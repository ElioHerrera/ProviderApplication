package com.provider.controller;

import com.provider.entities.Perfil;
import com.provider.entities.Usuario;
import com.provider.image.ImageResizer;
import com.provider.repositories.PerfilRepository;
import com.provider.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/img")
@CrossOrigin(origins = "http://localhost:4200")
public class ImagenPerfilController {

    private static final String RUTA_DE_IMAGENES = "provider/src/main/resources/static/uploads/";

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> subirArchivo(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId) {
        Map<String, String> response = new HashMap<>();
        try {
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(userId);
            if (optionalUsuario.isPresent()) {
                Usuario usuario = optionalUsuario.get();
                Perfil perfil = usuario.getPerfil();

                Path rutaDeUsuario = Paths.get(RUTA_DE_IMAGENES, String.valueOf(userId));
                if (!Files.exists(rutaDeUsuario)) {
                    Files.createDirectories(rutaDeUsuario);
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("HHmmssSSS"); // Formato de hora (HH), minutos (mm), segundos (ss) y milisegundos (SSS)
                String formattedTime = dateFormat.format(new Date()); // Obtiene el tiempo actual formateado
                String fileName = formattedTime.substring(formattedTime.length() - 5) + "_" + file.getOriginalFilename();
                Path path = rutaDeUsuario.resolve(fileName);

                // Lee el archivo como un BufferedImage
                BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));

                // Redimensiona y recorta la imagen
                BufferedImage resizedAndCroppedImage = ImageResizer.resizeAndCropImage(originalImage, 320);

                // Guarda la imagen redimensionada y recortada
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(resizedAndCroppedImage, "jpg", baos);
                Files.write(path, baos.toByteArray(), StandardOpenOption.CREATE);

                perfil.setFotoPerfil(fileName);
                perfilRepository.save(perfil);

                response.put("message", "Archivo subido exitosamente");
                response.put("fileName", fileName);
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Usuario no encontrado");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
            response.put("message", "Error al subir el archivo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/uploads/{userId}/{fileName:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable Long userId, @PathVariable String fileName) {
        System.out.println("Se ha recibido una solicitud para servir el archivo: " + fileName + " del usuario: " + userId);

        // Primero intentar cargar el archivo desde el directorio del usuario
        Path userFilePath = Paths.get(RUTA_DE_IMAGENES, String.valueOf(userId)).resolve(fileName).normalize();

        System.out.println(userFilePath);
        try {
            Resource userResource = new UrlResource(userFilePath.toUri());
            if (userResource.exists() || userResource.isReadable()) {
                System.out.println("Se ha encontrado y se puede leer el archivo: " + fileName + " para el usuario: " + userId);
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(userResource);
            }
        } catch (MalformedURLException e) {
            System.out.println("Error al construir la URL del archivo: " + e.getMessage());
        }

        // Si el archivo no se encuentra en el directorio del usuario, intentar cargarlo desde el directorio genérico

        System.out.println("buscar imagen generica");
        Path defaultFilePath = Paths.get(RUTA_DE_IMAGENES, "default.png").normalize();

        System.out.println(defaultFilePath);
        try {
            Resource defaultResource = new UrlResource(defaultFilePath.toUri());
            if (defaultResource.exists() || defaultResource.isReadable()) {
                System.out.println("Se ha encontrado y se puede leer el archivo: " + fileName + " en el directorio genérico");
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(defaultResource);
            }
        } catch (MalformedURLException e) {
            System.out.println("Error al construir la URL del archivo: " + e.getMessage());
        }

        // Si no se encuentra el archivo ni en el directorio del usuario ni en el genérico, devolver una respuesta de error
        System.out.println("No se ha encontrado el archivo: " + fileName);
        return ResponseEntity.notFound().build();
    }




}