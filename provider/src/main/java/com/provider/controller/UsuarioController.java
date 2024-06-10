package com.provider.controller;

import com.provider.dto.UsuarioDTO;
import com.provider.services.PerfilService;
import com.provider.services.UsuarioService;
import com.provider.converter.UsuarioConverter;
import com.provider.entities.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PerfilService perfilService;



    @GetMapping("/actualizar/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuario(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = usuarioService.obtenerUsuarioOptionalPorId(id);
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.notFound().build(); // Usuario no encontrado
        }

        Usuario usuario = usuarioOptional.get();
        UsuarioDTO usuarioDTO = UsuarioConverter.entityToDTO(usuario);
        return ResponseEntity.ok(usuarioDTO);
    }



    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioById(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioOptionalPorId(id);
        return usuario.map(value -> ResponseEntity.ok(UsuarioConverter.entityToDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}/nombre")
    public ResponseEntity<UsuarioDTO> actualizarNombreUsuario(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String nuevoNombre = body.get("nuevoNombre");

        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        usuario.getPerfil().setNombre(nuevoNombre);
        Usuario usuarioActualizado = usuarioService.saveUser(usuario);
        UsuarioDTO usuarioActualizadoDTO = UsuarioConverter.entityToDTO(usuarioActualizado);

        return ResponseEntity.ok(usuarioActualizadoDTO);
    }

    @PutMapping("/{id}/apellido")
    public ResponseEntity<UsuarioDTO> actualizarApellido(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String nuevoApellido = body.get("nuevoApellido");

        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        usuario.getPerfil().setApellido(nuevoApellido);
        Usuario usuarioActualizado = usuarioService.saveUser(usuario);
        UsuarioDTO usuarioActualizadoDTO = UsuarioConverter.entityToDTO(usuarioActualizado);

        return ResponseEntity.ok(usuarioActualizadoDTO);
    }

    @PutMapping("/{id}/descripcion")
    public ResponseEntity<UsuarioDTO> actualizarDescripcion(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String nuevaDescripcion = body.get("nuevaDescripcion");

        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        usuario.getPerfil().setDescripcion(nuevaDescripcion);
        Usuario usuarioActualizado = usuarioService.saveUser(usuario);
        UsuarioDTO usuarioActualizadoDTO = UsuarioConverter.entityToDTO(usuarioActualizado);

        return ResponseEntity.ok(usuarioActualizadoDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }

//        @GetMapping("")
//        public ResponseEntity<List<UsuarioDTO>> obtenerUsuarios() {
//        List<Usuario> usuarios = usuarioService.findAll();
//        List<UsuarioDTO> usuarioDTOs = usuarios.stream()
//                .map(UsuarioConverter::entityToDTO)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(usuarioDTOs);
//    }


}