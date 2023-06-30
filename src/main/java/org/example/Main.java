package org.example;

import org.example.Clases.Estudiante;
import org.example.controladores.CrudTradicionalControlador;
import org.example.servicios.BootStrapServices;
import org.example.servicios.EstudianteServices;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.bundled.RouteOverviewPlugin;

public class Main {

    //indica el modo de operacion para la base de datos.
    private static String modoConexion = "";

    public static void main(String[] args) {
        String mensaje = "Esta es la aplicacion 2";
        System.out.println(mensaje);
        if(args.length >= 1){
            modoConexion = args[0];
            System.out.println("Modo de Operacion: "+modoConexion);
        }

        //Iniciando la base de datos.
        if(modoConexion.isEmpty()) {
            BootStrapServices.getInstancia().init();
        }


        //Creando la instancia del servidor.
        Javalin app = Javalin.create(config ->{

            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/publico";
                staticFileConfig.location = Location.CLASSPATH;
            }); //desde la carpeta de resources

            config.plugins.register(new RouteOverviewPlugin("/rutas")); //aplicando plugins de las rutas
            config.plugins.enableCors(corsContainer -> {
                corsContainer.add(corsPluginConfig -> {
                    corsPluginConfig.anyHost();
                });
            });
        }).start(getHerokuAssignedPort());

        //Endpoint de inicio.
        app.get("/", ctx -> {
            ctx.result(mensaje);
        });

        app.get("/prueba", ctx -> {
            EstudianteServices.getInstancia().pruebaActualizacion();
            ctx.result("Bien!...");
        });

        //new CrudTradicionalControlador(app).aplicarRutas();
    }

    /**
     * Metodo para indicar el puerto en Heroku
     * @return
     */
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 7001; //Retorna el puerto por defecto en caso de no estar en Heroku.
    }

    /**
     * Nos
     * @return
     */
    public static String getModoConexion(){
        return modoConexion;
    }
}
