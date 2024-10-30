package com.provider.controller;

import com.provider.entities.Perfil;
import com.provider.entities.Producto;
import com.provider.entities.Usuario;
import com.provider.other.ImageResizer;
import com.provider.other.Consola;
import com.provider.services.PerfilService;
import com.provider.services.ProductoService;
import com.provider.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/img")
public class ImagenController {

    /*
    @Value("${gcp.bucket.name}")
    private String bucketName;
    private final Storage storage = StorageOptions.getDefaultInstance().getService();
    private static final String DEFAULT_PRODUCT_IMAGE = "uploads/default/defaultProduct.jpg";
    private static final String DEFAULT_PROFILE_IMAGE = "uploads/default/default.png";
    private static final String RUTA_DE_IMAGENES_PRODUCTOS = "provider/src/main/resources/static/uploads/";
     */


    private static final String RUTA_DE_IMAGENES = "provider/src/main/resources/static/uploads/";

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private PerfilService perfilService;

    @Autowired
    private ProductoService productoService;



    // CONFIGURACION para "http://localhost:4200"

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> guardarImagenlocal(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId) {

        Map<String, String> response = new HashMap<>();
        try {
            Optional<Usuario> optionalUsuario = usuarioService.obtenerUsuarioOptionalPorId(userId);
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

                BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(file.getBytes())); // Lee el archivo como un BufferedImage
                BufferedImage resizedAndCroppedImage = ImageResizer.resizeAndCropImage(originalImage, 320); // Redimensiona y recorta la imagen

                // Guarda la imagen redimensionada y recortada
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(resizedAndCroppedImage, "jpg", baos);
                Files.write(path, baos.toByteArray(), StandardOpenOption.CREATE);

                perfil.setFotoPerfil(fileName);
                perfilService.guardarPerfil(perfil);

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
    public ResponseEntity<Resource> obtenerImagenPerfilLocal(@PathVariable Long userId, @PathVariable String fileName) {

        Path userFilePath = Paths.get(RUTA_DE_IMAGENES, String.valueOf(userId)).resolve(fileName).normalize();
        try {
            Resource userResource = new UrlResource(userFilePath.toUri());
            if (userResource.exists() || userResource.isReadable()) {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(userResource);
            }
        } catch (MalformedURLException e) {
            System.out.println("Error al construir la URL del archivo: " + e.getMessage());
        }

        // Si el archivo no se encuentra en el directorio del usuario, intentar cargarlo desde el directorio genérico
        Path defaultFilePath = Paths.get(RUTA_DE_IMAGENES, "default.png").normalize();
        try {
            Resource defaultResource = new UrlResource(defaultFilePath.toUri());
            if (defaultResource.exists() || defaultResource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(defaultResource);
            }
        } catch (MalformedURLException e) {
            System.out.println("Error al construir la URL del archivo: " + e.getMessage());
        }
        System.out.println("No se ha encontrado el archivo: " + fileName);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/product/upload")
    public ResponseEntity<Map<String, String>> guardarImagenProductoLocal(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId, @RequestParam("productoId") Long productoId) {

        Map<String, String> response = new HashMap<>();
        try {
            Optional<Usuario> optionalUsuario = usuarioService.obtenerUsuarioOptionalPorId(userId);
            if (optionalUsuario.isPresent()) {
                Usuario usuario = optionalUsuario.get();
                Producto producto = usuario.getPerfil().getEmpresa().getProductos().stream()
                        .filter(p -> p.getId().equals(productoId))
                        .findFirst()
                        .orElse(null);

                if (producto == null) {
                    response.put("message", "Producto no encontrado");
                    return ResponseEntity.badRequest().body(response);
                }

                Path rutaDeProducto = Paths.get(RUTA_DE_IMAGENES, String.valueOf(userId));
                if (!Files.exists(rutaDeProducto)) {
                    Files.createDirectories(rutaDeProducto);
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("HHmmssSSS");
                String formattedTime = dateFormat.format(new Date());
                String fileName = formattedTime.substring(formattedTime.length() - 5) + "_" + file.getOriginalFilename();
                Path path = rutaDeProducto.resolve(fileName);

                BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
                BufferedImage resizedAndCroppedImage = ImageResizer.resizeAndCropImage(originalImage, 320);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(resizedAndCroppedImage, "jpg", baos);
                Files.write(path, baos.toByteArray(), StandardOpenOption.CREATE);

                producto.setFotoProducto(fileName);
                // Guardar cambios en la base de datos
                usuarioService.guardarUsuario(usuario);

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

    @GetMapping("/product/uploads/{userId}/{fileName:.+}")
    public ResponseEntity<Resource> obtenerImagenProductoLocal(@PathVariable Long userId, @PathVariable String fileName) {

        Path userFilePath = Paths.get(RUTA_DE_IMAGENES, String.valueOf(userId)).resolve(fileName).normalize();
        try {
            Resource userResource = new UrlResource(userFilePath.toUri());
            if (userResource.exists() || userResource.isReadable()) {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(userResource);
            }
        } catch (MalformedURLException e) {
            System.out.println("Error al construir la URL del archivo: " + e.getMessage());
        }

        // Si el archivo no se encuentra en el directorio del usuario, intentar cargarlo desde el directorio genérico
        Path defaultFilePath = Paths.get(RUTA_DE_IMAGENES, "defaultProduct.jpg").normalize();
        try {
            Resource defaultResource = new UrlResource(defaultFilePath.toUri());
            if (defaultResource.exists() || defaultResource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(defaultResource);
            }
        } catch (MalformedURLException e) {
            //System.out.println("Error al construir la URL del archivo: " + e.getMessage());
            Consola.aletaRoja("ALERTA", "Error al construir la URL del archivo: ", e.getMessage());
        }
        Consola.aletaRoja("ALERTA", "No se ha encontrado el archivo ",fileName);
        return ResponseEntity.notFound().build();
    }

    //CONFIGURATIÓN para "https://provider-pedidos-app.web.app"
    /*
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> guardarImagenPerfil(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId) {

        Map<String, String> respuesta = new HashMap<>();

        try {
            Optional<Usuario> optionalUsuario = usuarioService.obtenerUsuarioOptionalPorId(userId);
            if (optionalUsuario.isPresent()) {
                Usuario usuario = optionalUsuario.get();
                Perfil perfil = usuario.getPerfil();

                // Generar un nombre de archivo único -> Extraer los últimos 4 caracteres + "-" + fileName
                SimpleDateFormat formatoFecha = new SimpleDateFormat("HHmmssSSS");
                String horaFormateada = formatoFecha.format(new Date());
                String fileName = horaFormateada.substring(horaFormateada.length() - 4) + "_" + file.getOriginalFilename();

                // Redimensionar y recortar la imagen -> ( TargetSize = 320 px )
                BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
                BufferedImage imagenRedimensionadaYCortada = ImageResizer.resizeAndCropImage(originalImage, 320);

                // Convertir BufferedImage a byte array
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(imagenRedimensionadaYCortada, "jpg", baos);
                byte[] bytesImagen = baos.toByteArray();

                // Subir la imagen a Google Cloud Storage
                BlobId blobId = BlobId.of(bucketName, "uploads/" + userId + "/" + fileName);
                BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
                storage.create(blobInfo, bytesImagen);

                // Hacer público el objeto subido -> Dar Permisos Publicos
                storage.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

                // Actualizar el perfil del usuario con el nombre de la imagen
                perfil.setFotoPerfil(fileName);
                perfilService.guardarPerfil(perfil);

                respuesta.put("message", "Archivo subido exitosamente");
                respuesta.put("fileName", fileName);
                return ResponseEntity.ok(respuesta);
            } else {
                respuesta.put("message", "Usuario no encontrado");
                return ResponseEntity.badRequest().body(respuesta);
            }
        } catch (IOException e) {
            e.printStackTrace();
            respuesta.put("message", "Error al subir el archivo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @GetMapping("/uploads/{userId}/{fileName:.+}")
    public ResponseEntity<UrlResource> obtenerImagenPerfil(@PathVariable Long userId, @PathVariable String fileName) {
        try {
            // Obtener el objeto Blob de Google Cloud Storage
            Blob blob = storage.get(BlobId.of(bucketName, "uploads/" + userId + "/" + fileName));

            if (blob != null && blob.exists()) {
                // Crear y devolver la respuesta con el recurso de la imagen JPEG
                UrlResource recurso = new UrlResource(blob.getMediaLink());
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(recurso);
            } else {
                System.out.println("Imagen no encontrada en Google Cloud Storage, sirviendo imagen predeterminada.");

                // Cargar imagen predeterminada desde el bucket
                Blob blobPredeterminado = storage.get(BlobId.of(bucketName, DEFAULT_PROFILE_IMAGE));

                if (blobPredeterminado != null && blobPredeterminado.exists()) {
                    // Crear y devolver la respuesta con el recurso de la imagen PNG
                    UrlResource recurso = new UrlResource(blobPredeterminado.getMediaLink());
                    return ResponseEntity.ok()
                            .contentType(MediaType.IMAGE_PNG)
                            .body(recurso);
                } else {
                    System.out.println("Imagen predeterminada no encontrada en Google Cloud Storage.");
                }
            }
        } catch (MalformedURLException e) {
            System.out.println("Error al construir la URL del archivo: " + e.getMessage());
        }

        System.out.println("No se ha encontrado el archivo: " + fileName);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("product/upload")
    public ResponseEntity<Map<String, String>> guardarImagenProducto(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId, @RequestParam("productoId") Long productoId) {

        Map<String, String> respuesta = new HashMap<>();

        try {
            Optional<Usuario> optionalUsuario = usuarioService.obtenerUsuarioOptionalPorId(userId);
            if (!optionalUsuario.isPresent()) {
                respuesta.put("message", "Usuario no encontrado");
                return ResponseEntity.badRequest().body(respuesta);
            }
            Optional<Producto> optionalProducto = productoService.obtenerProductoOpcionalPorId(productoId);
            if (!optionalProducto.isPresent()) {
                respuesta.put("message", "Producto no encontrado");
                return ResponseEntity.badRequest().body(respuesta);
            }

            // Generar un nombre de archivo único -> Extraer los últimos 4 caracteres + "-" + fileName
            SimpleDateFormat formatoFecha = new SimpleDateFormat("HHmmssSSS");
            String horaFormateada = formatoFecha.format(new Date());
            String fileName = horaFormateada.substring(horaFormateada.length() - 4) + "_" + file.getOriginalFilename();

            // Redimensionar y recortar la imagen -> ( TargetSize = 480 px )
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
            BufferedImage imagenRedimensionadaYCortada = ImageResizer.resizeAndCropImage(originalImage, 480);

            // Convertir BufferedImage a byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(imagenRedimensionadaYCortada, "jpg", baos);
            byte[] bytesImagen = baos.toByteArray();

            // Subir la imagen a Google Cloud Storage
            BlobId blobId = BlobId.of(bucketName, "uploads/" + userId + "/product/" + fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
            storage.create(blobInfo, bytesImagen);

            // Hacer público el objeto subido -> Dar Permisos Publicos
            storage.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

            //Actualizar producto
            Producto producto = optionalProducto.get();
            producto.setFotoProducto(fileName);
            productoService.guardarProducto(producto);

            respuesta.put("message", "Archivo subido exitosamente y nombre de producto Actualizado");
            respuesta.put("fileName", fileName);

            return ResponseEntity.ok(respuesta);

        } catch (IOException e) {
            e.printStackTrace();
            respuesta.put("message", "Error al subir el archivo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @GetMapping("/product/uploads/{userId}/{fileName:.+}")
    public ResponseEntity<UrlResource> obtenerImagenProducto(@PathVariable Long userId, @PathVariable String fileName, HttpServletResponse response) {

        try {
            // Obtener el objeto Blob de Google Cloud Storage
            Blob blob = storage.get(BlobId.of(bucketName, "uploads/" + userId + "/product/" + fileName));
            if (blob != null && blob.exists()) {

                // Devolver la respuesta con el recurso de la imagen PNG
                UrlResource recurso = new UrlResource(blob.getMediaLink());
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(recurso);
            } else {
                System.out.println("Imagen no encontrada en Google Cloud Storage, sirviendo imagen predeterminada.");

                // Cargar imagen predeterminada desde el bucket
                Blob defaultBlob = storage.get(BlobId.of(bucketName, DEFAULT_PRODUCT_IMAGE));
                if (defaultBlob != null && defaultBlob.exists()) {

                    // Devolver la respuesta con el recurso de la imagen PNG
                    UrlResource recurso = new UrlResource(defaultBlob.getMediaLink());
                    return ResponseEntity.ok()
                            .contentType(MediaType.IMAGE_JPEG)
                            .body(recurso);
                } else {
                    System.out.println("Imagen predeterminada no encontrada en Google Cloud Storage.");
                }
            }
        } catch (MalformedURLException e) {
            System.out.println("Error al construir la URL del archivo producto: " + e.getMessage());
        }

        System.out.println("No se ha encontrado el archivo: " + fileName);
        return ResponseEntity.notFound().build();
    }
*/

}