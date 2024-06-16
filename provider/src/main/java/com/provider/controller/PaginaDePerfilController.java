package com.provider.controller;

import com.provider.converter.UsuarioConverter;
import com.provider.dto.UsuarioDTO;
import com.provider.entities.Usuario;
import com.provider.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/perfil")
public class PaginaDePerfilController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/{username}")
    public ResponseEntity<UsuarioDTO> getUserProfile(@PathVariable String username) {
        Usuario usuario = usuarioService.findByUsername(username);
        if (usuario != null) {
            UsuarioDTO usuarioDTO = UsuarioConverter.entityToDTO(usuario);
            return ResponseEntity.ok(usuarioDTO);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }
}