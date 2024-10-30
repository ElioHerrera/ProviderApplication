package com.provider.other;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

public class Comments {

    /*

    * CONFIGURACIÓN CLASS WebConfig

    * cors para firebase
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") //Todas las rutas en el backend permiten solicitudes CORS.
                .allowedOrigins("https://provider-pedidos-app.web.app") // Permite solicitudes CORS solo desde la URL "https://provider-pedidos-app.web.app".
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos HTTP permitidos.
                .allowedHeaders("*") // Permite todos los encabezados.
                .exposedHeaders("Authorization")  // Expone el encabezado Authorization para que el cliente pueda acceder.
                .allowCredentials(true) // Permite el envío de cookies y credenciales junto con la solicitud CORS.
                .maxAge(3600); // Tiempo en segundos. indica que las respuestas de las solicitudes preflight pueden ser almacenadas en caché por el navegador durante 1 hora.
    }

    // configuración de ruta para recursos estáticos
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**") // Cualquier solicitud que empiece con '/uploads/' será manejada por este recurso
                .addResourceLocations("file:provider/src/main/resources/static/uploads/") // Ubicación de archivos de los recursos.
                .setCachePeriod(0); // Para deshabilitar la caché
    }










    */
}
