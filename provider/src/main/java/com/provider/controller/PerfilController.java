package com.provider.controller;

import com.provider.converter.UsuarioConverter;
import com.provider.entities.Usuario;
import com.provider.dto.PerfilRelacionDTO;
import com.provider.dto.UsuarioDTO;
import com.provider.services.PerfilService;
import com.provider.services.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/perfiles")
public class PerfilController {

    @Autowired
    private PerfilService perfilService;
    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/{perfilId}/relaciones-comerciales")
    public ResponseEntity<List<PerfilRelacionDTO>> obtenerRelacionesComerciales(@PathVariable("perfilId") Long perfilId) {
       System.out.println("     METODO : CLASS PerfilController : obtenerRelacionesComerciales()");
       List<PerfilRelacionDTO> relacionesComerciales = perfilService.obtenerRelacionesComerciales(perfilId);
       return ResponseEntity.ok().body(relacionesComerciales);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UsuarioDTO> getUserProfile(@PathVariable String username) {
        Usuario usuario = usuarioService.obtenerUsuarioPorUsername(username);
        if (usuario != null) {
            UsuarioDTO usuarioDTO = UsuarioConverter.entityToDTO(usuario);
            return ResponseEntity.ok(usuarioDTO);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }
}