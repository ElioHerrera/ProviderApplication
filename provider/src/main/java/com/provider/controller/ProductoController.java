package com.provider.controller;

import com.provider.converter.ProductoConverter;
import com.provider.dto.ProductoDTO;
import com.provider.entities.*;
import com.provider.image.ImageResizer;
import com.provider.services.*;
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
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/producto")
public class ProductoController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoConverter productoConverter;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private PrecioService precioService;

    @Autowired
    private ListaPrecioService listaPrecioService;

    private static final String RUTA_DE_IMAGENES_PRODUCTOS = "provider/src/main/resources/static/uploads/";//ANTERIOR

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> guardarImagenProducto(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId, @RequestParam("productoId") Long productoId) {

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

                Path rutaDeProducto = Paths.get(RUTA_DE_IMAGENES_PRODUCTOS, String.valueOf(userId));
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

    @GetMapping("/uploads/{userId}/{fileName:.+}")
    public ResponseEntity<Resource> obtenerImagenProducto(@PathVariable Long userId, @PathVariable String fileName) {

        Path userFilePath = Paths.get(RUTA_DE_IMAGENES_PRODUCTOS, String.valueOf(userId)).resolve(fileName).normalize();
        try {
            Resource userResource = new UrlResource(userFilePath.toUri());
            if (userResource.exists() || userResource.isReadable()) {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(userResource);
            }
        } catch (MalformedURLException e) {
            System.out.println("Error al construir la URL del archivo: " + e.getMessage());
        }

        // Si el archivo no se encuentra en el directorio del usuario, intentar cargarlo desde el directorio genérico
        Path defaultFilePath = Paths.get(RUTA_DE_IMAGENES_PRODUCTOS, "defaultProduct.jpg").normalize();
        try {
            Resource defaultResource = new UrlResource(defaultFilePath.toUri());
            if (defaultResource.exists() || defaultResource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(defaultResource);
            }
        } catch (MalformedURLException e) {
            System.out.println("Error al construir la URL del archivo: " + e.getMessage());
        }
        System.out.println("No se ha encontrado el archivo: " + fileName);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("lista/{userId}")
    public ResponseEntity<List<ProductoDTO>> obtenerProductosPorId(@PathVariable Long userId) {
        System.out.println("     METODO : CLASS ProductoController :  obtenerProductosPorId");

        Optional<Usuario> usuarioOptional = usuarioService.obtenerUsuarioOptionalPorId(userId);

        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.notFound().build(); // Usuario no encontrado
        }
        Usuario usuario = usuarioOptional.get();

        List<Producto> productos = usuario.getPerfil().getEmpresa().getProductos();
        // Convertir la List<Producto> productos a List<ProductoDTO> productosDTO
        List<ProductoDTO> productosDTO = productos.stream()
                .map(productoConverter::entityToDTO)  // Aplicar el convertidor
                .collect(Collectors.toList());

        return ResponseEntity.ok(productosDTO);
    }

    @PutMapping("/{id}/interruptor")
    public ResponseEntity<ProductoDTO> alternarHabilitado(@PathVariable Long id, @RequestBody Map<String, Boolean> requestBody) {
        Boolean isEnabled = requestBody.get("isEnabled");
        Optional<Producto> productoOptional = productoService.obtenerProductoOpcionalPorId(id);

        if (productoOptional.isEmpty()) {
            return ResponseEntity.notFound().build(); // Usuario no encontrado
        }

        Producto producto = productoOptional.get();
        producto.setEnabled(isEnabled); // Actualiza el estado del producto
        productoService.guardarProducto(producto);
        ProductoDTO dto = productoConverter.entityToDTO(producto);

        return ResponseEntity.ok(dto);

    }

    @PostMapping("/crear/{userId}")
    public ResponseEntity<ProductoDTO> crearProducto(@PathVariable("userId") Long userId) {
        System.out.println("     CLASS : ProductoController     METODO : crearProducto()");
        Usuario usuario = usuarioService.obtenerUsuarioPorId(userId);

        System.out.print("     ACTION : Usuario recuperado");

        if (usuario != null) {
            Empresa empresa = usuario.getPerfil().getEmpresa();

            Producto nuevoProducto = Producto.builder()
                    .codigo(9999)
                    .fotoProducto("defaultProduct.jpg")
                    .nombre("Nombre producto")
                    .descripcion("Descripción producto")
                    .isEnabled(false)
                    .empresa(empresa)
                    .build();
            productoService.guardarProducto(nuevoProducto);

            System.out.print("     ACTION : Producto creado");

            // Verificar y crear listas de precios si no existen

            List<ListaPrecio> listasDePrecios = empresa.getListasPrecios();
            if (listasDePrecios.isEmpty()) {
                for (int i = 1; i <= 3; i++) {
                    ListaPrecio nuevaLista = new ListaPrecio();
                    nuevaLista.setNombre("Lista " + i);
                    nuevaLista.setEmpresa(empresa);

                    listaPrecioService.guardarListaPrecio(nuevaLista);
                    listasDePrecios.add(nuevaLista);

                    System.out.print("     ACTION : Nueva lista");
                }
            }


            // Crear precios asociados al producto y a la lista de precio
            for (ListaPrecio lista : listasDePrecios) {
                Precio nuevoPrecio = new Precio();
                nuevoPrecio.setProducto(nuevoProducto);
                nuevoPrecio.setLista(lista);
                nuevoPrecio.setPrecio(0.0); // Valor inicial, puedes cambiar esto según sea necesario
                precioService.guardarPrecio(nuevoPrecio);

                System.out.print("     ACTION : Nuevo precio");
            }



            List<Producto> productosActualizados = empresa.getProductos();
            productosActualizados.add(nuevoProducto);
            empresa.setProductos(productosActualizados);
            empresaService.guardarEmpresa(empresa);



            ProductoDTO dto = productoConverter.entityToDTO(nuevoProducto);

            System.out.print("     ACTION : Devolver productos dto");

            if (dto != null) {
                return new ResponseEntity<>(dto, HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ProductoDTO> actualizarProducto(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {

        System.out.println("     CLASS : ProductoController     METODO : actualizarProducto()");
        Optional<Producto> productoOptional = productoService.obtenerProductoOpcionalPorId(id);
        if (productoOptional.isEmpty()) {
            return ResponseEntity.notFound().build(); // Producto no encontrado
        }

        Producto producto = productoOptional.get();
        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setCodigo(productoDTO.getCodigo());

        // Actualizar precios asociados
        List<Precio> precios = precioService.obtenerPreciosPorProducto(producto.getId());

        for (Precio precio : precios) {
            if (precio.getLista().getNombre().equals("Lista 1")) {
                precio.setPrecio(productoDTO.getLista1());
            } else if (precio.getLista().getNombre().equals("Lista 2")) {
                precio.setPrecio(productoDTO.getLista2());
            } else if (precio.getLista().getNombre().equals("Lista 3")) {
                precio.setPrecio(productoDTO.getLista3());
            }
            precioService.guardarPrecio(precio);
        }

        producto = productoService.guardarProducto(producto);
        ProductoDTO actualizado = productoConverter.entityToDTO(producto);

        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        // Obtener los precios asociados al producto
        List<Precio> preciosAsociados = precioService.obtenerPreciosPorProducto(id);

        // Eliminar los precios asociados
        for (Precio precio : preciosAsociados) {
            precioService.eliminarPrecioPorId(precio.getId());
        }
        // Eliminar el producto
        productoService.eliminarProducto(id);

        return ResponseEntity.noContent().build();
    }

}
