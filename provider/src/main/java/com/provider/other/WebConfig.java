package com.provider.other;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig  implements WebMvcConfigurer {

    //CONFIGURACIÓN CORS para Firebase
    /*
    addMapping("/**") >>> Todas las rutas en el backend permiten solicitudes CORS.
    allowedOrigins("https://provider-pedidos-app.web.app") >>> Permite solicitudes CORS solo desde la URL "https://provider-pedidos-app.web.app".
    allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") >>> Métodos HTTP permitidos.
    allowedHeaders("*") >>> Permite todos los encabezados.
    exposedHeaders("Authorization") >>> Expone el encabezado Authorization para que el cliente pueda acceder.
    allowCredentials(true) >>> Permite el envío de cookies y credenciales junto con la solicitud CORS.
    maxAge(3600) >>> Tiempo en segundos. indica que las respuestas de las solicitudes preflight pueden ser almacenadas en caché por el navegador durante 1 hora.
    */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://provider-pedidos-app.web.app")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization")  // Si necesitas exponer otros encabezados personalizados
                .allowCredentials(true)
                .maxAge(3600);
    }

    //CONFIGURACIÓN CORS para localhost:4200
        /*
    addMapping("/**") >>> Todas las rutas en el backend permiten solicitudes CORS.
    allowedOrigins("https://provider-pedidos-app.web.app") >>> Permite solicitudes CORS solo desde la URL "http://localhost:4200".
    allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") >>> Métodos HTTP permitidos.
    allowedHeaders("*") >>> Permite todos los encabezados.
    maxAge(3600) >>> Tiempo en segundos. indica que las respuestas de las solicitudes preflight pueden ser almacenadas en caché por el navegador durante 1 hora.
    */
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:4200")
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowedHeaders("*")
//                .maxAge(3600);
//    }

    //CONFIGURACIÓN de ruta para recursos estáticos
    /*
    addResourceHandler("/uploads/**") >>> cualquier solicitud que empiece con '/uploads/' será manejada por este recurso
    addResourceLocations("file:provider/src/main/resources/static/uploads/") >>> Ubicación de archivos de los recursos.
    setCachePeriod(0) >>> Para deshabilitar la caché
    */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:provider/src/main/resources/static/uploads/")
                .setCachePeriod(0);
    }
}
