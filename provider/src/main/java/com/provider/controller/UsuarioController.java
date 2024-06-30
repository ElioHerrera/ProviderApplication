package com.provider.controller;


import com.provider.dto.PerfilRelacionDTO;
import com.provider.dto.UsuarioDTO;
import com.provider.services.PerfilService;
import com.provider.services.UsuarioService;
import com.provider.converter.UsuarioConverter;
import com.provider.converter.PerfilRelacionConverter;
import com.provider.entities.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioById(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioOptionalPorId(id);
        return usuario.map(value -> ResponseEntity.ok(UsuarioConverter.entityToDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/actualizar/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuario(@PathVariable Long id) {
        System.out.println("     METODO : CLASS UsuarioController :obtenerUsuario()");
        Optional<Usuario> usuarioOptional = usuarioService.obtenerUsuarioOptionalPorId(id);

        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.notFound().build(); // Usuario no encontrado
        }

        Usuario usuario = usuarioOptional.get();
        UsuarioDTO usuarioDTO = UsuarioConverter.entityToDTO(usuario);
        return ResponseEntity.ok(usuarioDTO);
    }

    @PutMapping("/{id}/nombre")
    public ResponseEntity<UsuarioDTO> actualizarNombre(@PathVariable Long id, @RequestBody Map<String, String> body) {
        System.out.println("     METODO : CLASS UsuarioController : actualizarNombre()");
        String nuevoNombre = body.get("nuevoNombre");

        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        usuario.getPerfil().setNombre(nuevoNombre);
        Usuario usuarioActualizado = usuarioService.guardarUsuario(usuario);
        UsuarioDTO usuarioActualizadoDTO = UsuarioConverter.entityToDTO(usuarioActualizado);

        return ResponseEntity.ok(usuarioActualizadoDTO);
    }

    @PutMapping("/{id}/apellido")
    public ResponseEntity<UsuarioDTO> actualizarApellido(@PathVariable Long id, @RequestBody Map<String, String> body) {
        System.out.println("     METODO : CLASS UsuarioController : actualizarApellido()");
        String nuevoApellido = body.get("nuevoApellido");

        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        usuario.getPerfil().setApellido(nuevoApellido);
        Usuario usuarioActualizado = usuarioService.guardarUsuario(usuario);
        UsuarioDTO usuarioActualizadoDTO = UsuarioConverter.entityToDTO(usuarioActualizado);

        return ResponseEntity.ok(usuarioActualizadoDTO);
    }

    @PutMapping("/{id}/descripcion")
    public ResponseEntity<UsuarioDTO> actualizarDescripcion(@PathVariable Long id, @RequestBody Map<String, String> body) {

        System.out.println("     METODO : CLASS UsuarioController : actualizarDescripcion()");
        String nuevaDescripcion = body.get("nuevaDescripcion");

        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        usuario.getPerfil().setDescripcion(nuevaDescripcion);
        Usuario usuarioActualizado = usuarioService.guardarUsuario(usuario);
        UsuarioDTO usuarioActualizadoDTO = UsuarioConverter.entityToDTO(usuarioActualizado);

        return ResponseEntity.ok(usuarioActualizadoDTO);
    }

    @GetMapping("proveedores")
    public ResponseEntity<?> obtenerProveedores() {

        System.out.println("     METODO : CLASS UsuarioController : obtenerProveedores()");
        List<Usuario> proveedores = usuarioService.obtenerProveedores();
        List<PerfilRelacionDTO> proveedoresDTO = new ArrayList<>();

        for (Usuario usuario : proveedores) {
            PerfilRelacionDTO proveedor = PerfilRelacionDTO.builder().build();
            proveedor = PerfilRelacionConverter.entityToDTO(usuario.getPerfil());
            //proveedorDTO = UsuarioConverter.entityToDTO(usuario);
            proveedoresDTO.add(proveedor);
        }
        if (proveedores.isEmpty()) {
            return ResponseEntity.ok("No hay proveedores disponibles.");
        } else {
            return ResponseEntity.ok(proveedoresDTO);
        }
    }

}