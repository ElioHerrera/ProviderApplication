package com.provider.controller;

import com.provider.converter.PublicacionConverter;
import com.provider.dto.PublicacionDTO;
import com.provider.entities.Perfil;
import com.provider.entities.Publicacion;
import com.provider.entities.Usuario;
import com.provider.services.PerfilService;
import com.provider.services.PublicacionService;
import com.provider.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/api/publicaciones")
public class PublicacionController {

    private static final String RUTA_DE_IMAGENES = "provider/src/main/resources/static/uploads/";

    @Autowired
    private PublicacionService publicacionService;

    @Autowired
    private PerfilService perfilService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/crear")
    public ResponseEntity<?> crearPublicacion(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId, @RequestParam("descripcion") String descripcion) {

        Optional<Usuario> optionalUsuario = usuarioService.obtenerUsuarioOptionalPorId(userId);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            Perfil autor = usuario.getPerfil();

            if (autor != null) {
                // Crear directorio si no existe
                Path rutaDeUsuario = Paths.get(RUTA_DE_IMAGENES, String.valueOf(userId));
                if (!Files.exists(rutaDeUsuario)) {
                    try {
                        Files.createDirectories(rutaDeUsuario);
                    } catch (IOException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear directorio: " + e.getMessage());
                    }
                }

                // Generar nombre único para el archivo
                SimpleDateFormat dateFormat = new SimpleDateFormat("HHmmssSSS");
                String formattedTime = dateFormat.format(new Date());
                String fileName = formattedTime.substring(formattedTime.length() - 5) + "_" + file.getOriginalFilename();
                Path path = rutaDeUsuario.resolve(fileName);

                try {
                    Files.write(path, file.getBytes(), StandardOpenOption.CREATE);
                } catch (IOException e) {
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir el archivo: " + e.getMessage());
                }

                // Crear la nueva publicación
                Publicacion publicacion = Publicacion.builder()
                        .contenido(descripcion)
                        .fotoPublicacion(fileName)
                        .likes(0)
                        .fecha(new Date())
                        .autor(autor)
                        .build();

                Publicacion nuevaPublicacion = publicacionService.save(publicacion);

                autor.getPublicaciones().add(nuevaPublicacion);
                perfilService.guardarPerfil(autor);

                return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPublicacion);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Perfil del autor no encontrado.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
        }
    }

    @PostMapping("/cargar")
    public ResponseEntity<Map<String, String>> guardarPublicacionLocal(@RequestParam("userId") Long userId, @RequestParam("file") MultipartFile file) {

        Map<String, String> response = new HashMap<>();
        try {
            Optional<Usuario> optionalUsuario = usuarioService.obtenerUsuarioOptionalPorId(userId);
            if (optionalUsuario.isPresent()) {
                Usuario usuario = optionalUsuario.get();

                Path rutaDeUsuario = Paths.get(RUTA_DE_IMAGENES, String.valueOf(userId));
                if (!Files.exists(rutaDeUsuario)) {
                    Files.createDirectories(rutaDeUsuario);
                }

                String fileName = file.getOriginalFilename();
                Path path = rutaDeUsuario.resolve(fileName);

                BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(originalImage, "jpg", baos);
                Files.write(path, baos.toByteArray(), StandardOpenOption.CREATE);

                response.put("message", "Archivo subido exitosamente");
                response.put("fileName", fileName);
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Perfil no encontrado");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
            response.put("message", "Error al subir el archivo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> obtenerPublicacionesPorPerfilyAsociados(@PathVariable Long userId) {

        try {
            Usuario usuario = usuarioService.obtenerUsuarioPorId(userId);

            if (usuario == null || usuario.getPerfil() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario o perfil no encontrado.");
            }

            Perfil perfil = usuario.getPerfil();
            List<PublicacionDTO> publicacionesDTO = new ArrayList<>();

            // Obtener publicaciones del perfil
            List<Publicacion> publicacionesPerfil = perfil.getPublicaciones();
            if (publicacionesPerfil != null) {
                for (Publicacion publicacion : publicacionesPerfil) {
                    PublicacionDTO publicacionDTO = PublicacionConverter.entityToDTO(publicacion);
                    publicacionesDTO.add(publicacionDTO);
                }
            }

            // Obtener las relaciones comerciales del perfil
            Set<Perfil> relacionesComerciales = perfil.getRelacioneComerciales();
            if (relacionesComerciales != null) {
                for (Perfil perfilRelacion : relacionesComerciales) {
                    List<Publicacion> publicacionesRelaciones = perfilRelacion.getPublicaciones();
                    if (publicacionesRelaciones != null) {
                        for (Publicacion publicacionPerfilrelacion : publicacionesRelaciones) {
                            PublicacionDTO dto = PublicacionConverter.entityToDTO(publicacionPerfilrelacion);
                            publicacionesDTO.add(dto);
                        }
                    }
                }
            }

            // Ordenar publicaciones por fecha descendente
            publicacionesDTO.sort(Comparator.comparing(PublicacionDTO::getFecha).reversed());

            return ResponseEntity.ok(publicacionesDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor: " + e.getMessage());
        }

    }

    @GetMapping("/id/{perfilId}")
    public ResponseEntity<?> obtenerPublicacionesDelPerfil(@PathVariable Long perfilId) {
        Perfil perfil = perfilService.obtenerPerfilPorId(perfilId).orElse(null);

        if (perfil != null) {

            List<PublicacionDTO> publicacionesDTO = new ArrayList<>();
            List<Publicacion> publicacionesPerfil = perfil.getPublicaciones();


            for (Publicacion publicacion : publicacionesPerfil) {
                PublicacionDTO publicacionDTO = PublicacionConverter.entityToDTO(publicacion);
                publicacionesDTO.add(publicacionDTO);
            }

            // Ordenar publicaciones por fecha descendente
            publicacionesDTO.sort(Comparator.comparing(PublicacionDTO::getFecha).reversed());


            return ResponseEntity.ok(publicacionesDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Perfil no encontrado.");
        }
    }

    @DeleteMapping("/{publicacionId}/eliminar")
    public ResponseEntity<?> eliminarPublicacion(@PathVariable Long publicacionId) {
        try {
            Optional<Publicacion> optionalPublicacion = publicacionService.obtenerPublicacionOptionaPorId(publicacionId);
            if (optionalPublicacion.isPresent()) {
                Publicacion publicacion = optionalPublicacion.get();

                // Retirar la publicación de la lista de publicaciones del perfil del usuario
                Perfil perfil = publicacion.getAutor(); // Obtener el perfil del autor de la publicación
                if (perfil != null) {
                    perfil.getPublicaciones().remove(publicacion); // Eliminar la publicación de la lista
                    perfilService.guardarPerfil(perfil); // Guardar el perfil actualizado
                }
                // Eliminar la publicación de la base de datos
                publicacionService.eliminar(publicacion.getId());

                // Devolver un objeto JSON como respuesta
                return ResponseEntity.ok().body(Map.of("message", "Publicación eliminada exitosamente"));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la publicación: " + e.getMessage());
        }
    }

    @PostMapping("/{publicacionId}/like")
    public ResponseEntity<?> darLike(@PathVariable Long publicacionId) {
        Optional<Publicacion> publicacionOptional = publicacionService.obtenerPublicacionOptionaPorId(publicacionId);

        if (publicacionOptional.isPresent()) {
            Publicacion publicacion = publicacionOptional.get();
            publicacion.setLikes(publicacion.getLikes() + 1);
            publicacionService.save(publicacion);
            return ResponseEntity.ok(publicacion);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Publicación no encontrada.");
        }
    }

}