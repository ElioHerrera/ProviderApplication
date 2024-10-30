package com.provider.controller;


import com.provider.converter.PerfilRelacionConverter;
import com.provider.converter.UsuarioConverter;
import com.provider.dto.PerfilRelacionDTO;
import com.provider.dto.UsuarioDTO;
import com.provider.entities.Usuario;
import com.provider.other.Consola;
import com.provider.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorId(@PathVariable Long id) {

        Optional<Usuario> usuario = usuarioService.obtenerUsuarioOptionalPorId(id);

        return usuario.map(value -> ResponseEntity.ok(UsuarioConverter.entityToDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        try {
            Consola.amarillo("actualizaUsuario()");
            UsuarioDTO dto = usuarioDTO;
            imprimirDatosActualizados(id, dto);
            UsuarioDTO usuarioActualizado = usuarioService.actualizarUsuario(id, dto);
            return ResponseEntity.ok(usuarioActualizado);

        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build(); // En caso de que el usuario no se encuentre

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Para manejar otros errores
        }
    }

    @GetMapping("proveedores")
    public ResponseEntity<?> obtenerProveedores() {

        Consola.rosa("obtenerProveedores()");
        List<Usuario> proveedores = usuarioService.obtenerProveedores();
        List<PerfilRelacionDTO> proveedoresDTO = new ArrayList<>();

        for (Usuario usuario : proveedores) {
            PerfilRelacionDTO proveedorDTO = PerfilRelacionDTO.builder().build();
            proveedorDTO = PerfilRelacionConverter.entityToDTO(usuario.getPerfil());

            proveedoresDTO.add(proveedorDTO);
        }
        if (proveedores.isEmpty()) {
            return ResponseEntity.ok("No hay proveedores disponibles.");
        } else {
            return ResponseEntity.ok(proveedoresDTO);
        }
    }

    private void imprimirDatosActualizados(Long id, UsuarioDTO dto){
        Consola.azul("Id recibido: " + id);
        Consola.rojo("Nombre: " + dto.getPerfil().getNombre());
        Consola.rojo("Apellido: " + dto.getPerfil().getApellido());
        Consola.rojo("Email: " + dto.getEmail());
        Consola.rojo("Descripcion: " + dto.getPerfil().getDescripcion());


        if (dto.getPerfil().getEmpresa() != null) {
            Consola.rojo("Empresa: " + dto.getPerfil().getEmpresa().getNombre());
            Consola.rojo("Rubro: " + dto.getPerfil().getEmpresa().getRubro());
            Consola.rojo("Domicilio: " + dto.getPerfil().getEmpresa().getDomicilio());
            Consola.rojo("Teléfono: " + dto.getPerfil().getEmpresa().getTelefono());
        }
        if(dto.getPerfil().getComercio() != null){
            Consola.rojo("Comercio: " + dto.getPerfil().getComercio().getNombre());
            Consola.rojo("Rubro: " + dto.getPerfil().getComercio().getRubro());
            Consola.rojo("Domicilio: " + dto.getPerfil().getComercio().getDomicilio());
            Consola.rojo("Teléfono: " + dto.getPerfil().getComercio().getTelefono());

        }
    }

    //    @PutMapping("/{id}/nombre")
//    public ResponseEntity<UsuarioDTO> actualizarNombre(@PathVariable Long id, @RequestBody Map<String, String> body) {
//        Consola.amarillo("actualizarNombre()");
//        String nuevoNombre = body.get("nuevoNombre");
//
//        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
//        if (usuario == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        usuario.getPerfil().setNombre(nuevoNombre);
//        Usuario usuarioActualizado = usuarioService.guardarUsuario(usuario);
//        UsuarioDTO usuarioActualizadoDTO = UsuarioConverter.entityToDTO(usuarioActualizado);
//
//        return ResponseEntity.ok(usuarioActualizadoDTO);
//    }

//    @PutMapping("/{id}/apellido")
//    public ResponseEntity<UsuarioDTO> actualizarApellido(@PathVariable Long id, @RequestBody Map<String, String> body) {
//        Consola.amarillo("actualizarApellido()");
//        String nuevoApellido = body.get("nuevoApellido");
//
//        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
//        if (usuario == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        usuario.getPerfil().setApellido(nuevoApellido);
//        Usuario usuarioActualizado = usuarioService.guardarUsuario(usuario);
//        UsuarioDTO usuarioActualizadoDTO = UsuarioConverter.entityToDTO(usuarioActualizado);
//
//        return ResponseEntity.ok(usuarioActualizadoDTO);
//    }

//    @PutMapping("/{id}/descripcion")
//    public ResponseEntity<UsuarioDTO> actualizarDescripcion(@PathVariable Long id, @RequestBody Map<String, String> body) {
//
//        Consola.amarillo("actualizarDescripcion()");
//        String nuevaDescripcion = body.get("nuevaDescripcion");
//
//        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
//        if (usuario == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        usuario.getPerfil().setDescripcion(nuevaDescripcion);
//        Usuario usuarioActualizado = usuarioService.guardarUsuario(usuario);
//        UsuarioDTO usuarioActualizadoDTO = UsuarioConverter.entityToDTO(usuarioActualizado);
//
//        return ResponseEntity.ok(usuarioActualizadoDTO);
//    }



//    @GetMapping("/actualizar/{id}")
//    public ResponseEntity<UsuarioDTO> obtenerUsuario(@PathVariable Long id) {
//
//        Optional<Usuario> usuarioOptional = usuarioService.obtenerUsuarioOptionalPorId(id);
//
//        if (usuarioOptional.isEmpty()) {
//            return ResponseEntity.notFound().build(); // Usuario no encontrado
//        }
//
//        Usuario usuario = usuarioOptional.get();
//        UsuarioDTO usuarioDTO = UsuarioConverter.entityToDTO(usuario);
//        return ResponseEntity.ok(usuarioDTO);
//    }


}