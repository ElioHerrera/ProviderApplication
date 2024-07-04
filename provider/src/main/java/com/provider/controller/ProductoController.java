package com.provider.controller;

import com.google.cloud.storage.*;
import com.provider.converter.ProductoConverter;
import com.provider.converter.ProductoProveedorConverter;
import com.provider.dto.ProductoDTO;
import com.provider.dto.ProductoProveedorDTO;
import com.provider.entities.*;
import com.provider.other.ImageResizer;
import com.provider.services.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

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
@AllArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/api/producto")
public class ProductoController {

    private UsuarioService usuarioService;
    private ProductoService productoService;
    private ProductoConverter productoConverter;
    private EmpresaService empresaService;
    private PrecioService precioService;
    private ListaPrecioService listaPrecioService;


    @GetMapping("lista/{userId}")
    public ResponseEntity<List<ProductoDTO>> obtenerProductosPorId(@PathVariable Long userId) {

        Optional<Usuario> usuarioOptional = usuarioService.obtenerUsuarioOptionalPorId(userId);

        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.notFound().build(); // Usuario no encontrado
        }
        Usuario usuario = usuarioOptional.get();

        List<Producto> productos = usuario.getPerfil().getEmpresa().getProductos();

        // Convertir la List<Producto> productos a List<ProductoDTO> productosDTO recorriendo y convirtiendo cada producto
        List<ProductoDTO> productosDTO = productos.stream()
                .map(productoConverter::entityToDTO)
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

        if (usuario != null) {
            Empresa empresa = usuario.getPerfil().getEmpresa();

            Producto nuevoProducto = Producto.builder()
                    .codigo(9999)
                    .fotoProducto("product.jpg")
                    .nombre("Nombre producto")
                    .descripcion("Descripción producto")
                    .isEnabled(false)
                    .empresa(empresa)
                    .build();
            productoService.guardarProducto(nuevoProducto);


            // Verificar y crear listas de precios si no existen
            List<ListaPrecio> listasDePrecios = empresa.getListasPrecios();
            if (listasDePrecios.isEmpty()) {
                for (int i = 1; i <= 3; i++) {
                    ListaPrecio nuevaLista = new ListaPrecio();
                    nuevaLista.setNombre("Lista " + i);
                    nuevaLista.setEmpresa(empresa);

                    listaPrecioService.guardarListaPrecio(nuevaLista);
                    listasDePrecios.add(nuevaLista);

                }
            }


            // Crear precios asociados al producto y a la lista de precio
            for (ListaPrecio lista : listasDePrecios) {
                Precio nuevoPrecio = new Precio();
                nuevoPrecio.setProducto(nuevoProducto);
                nuevoPrecio.setLista(lista);
                nuevoPrecio.setPrecio(0.0);
                precioService.guardarPrecio(nuevoPrecio);

            }



            List<Producto> productosActualizados = empresa.getProductos();
            productosActualizados.add(nuevoProducto);
            empresa.setProductos(productosActualizados);
            empresaService.guardarEmpresa(empresa);



            ProductoDTO dto = productoConverter.entityToDTO(nuevoProducto);

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

    @GetMapping("/tieneProductos/{userId}")
    public ResponseEntity<Boolean> tieneProductos(@PathVariable Long userId) {
        Optional<Usuario> usuarioOptional = usuarioService.obtenerUsuarioOptionalPorId(userId);

        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.notFound().build(); // Usuario no encontrado
        }
        Usuario usuario = usuarioOptional.get();

        List<Producto> productos = usuario.getPerfil().getEmpresa().getProductos();

        return ResponseEntity.ok(!productos.isEmpty());
    }

    @GetMapping("/productosProveedor/{userId}/{proveedorId}")
    public ResponseEntity<List<ProductoProveedorDTO>> obtenerProductosProveedor(@PathVariable Long userId, @PathVariable Long proveedorId) {

        Usuario usuario = usuarioService.obtenerUsuarioPorId(userId);
        Usuario proveedor = usuarioService.obtenerUsuarioPorId(proveedorId);

        if (usuario == null || proveedor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Obtener la lista de precios asignada al comercio del usuario para la empresa del proveedor
        Comercio comercio = usuario.getPerfil().getComercio();
        Empresa empresa = proveedor.getPerfil().getEmpresa();

        ListaPrecio listaAsignada = comercio.getListasAsignadas().stream()
                .filter(lista -> lista.getEmpresa().equals(empresa))
                .findFirst()
                .orElse(null);

        List<ProductoProveedorDTO> listaDto = new ArrayList<>();

        if (listaAsignada != null) {
            List<Producto> listaProveedor = empresa.getProductos();

            for (Producto producto : listaProveedor) {
                Precio precioProducto = listaAsignada.getPrecios().stream()
                        .filter(precio -> precio.getProducto().equals(producto))
                        .findFirst()
                        .orElse(null);

                if (precioProducto != null) {
                    ProductoProveedorDTO dto = ProductoProveedorConverter.entityToDTO(producto);
                    dto.setPrecio(precioProducto.getPrecio());
                    listaDto.add(dto);
                }
            }
        }

        return ResponseEntity.ok(listaDto);
    }

}
