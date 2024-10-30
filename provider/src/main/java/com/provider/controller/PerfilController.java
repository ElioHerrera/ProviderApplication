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

    //Método para obtener las relaciones comerciales
    @GetMapping("/{perfilId}/relaciones-comerciales")
    public ResponseEntity<List<PerfilRelacionDTO>> obtenerRelacionesComerciales(@PathVariable("perfilId") Long perfilId) {
       List<PerfilRelacionDTO> relacionesComerciales = perfilService.obtenerRelacionesComerciales(perfilId);
       return ResponseEntity.ok().body(relacionesComerciales);
    }


    @GetMapping("/{perfilId}/solicitudes-pendientes")
    public ResponseEntity<List<PerfilRelacionDTO>> obtenerUsuariosConSolicitudesPendientes(@PathVariable("perfilId") Long perfilId) {
        List<PerfilRelacionDTO> usuariosConSolicitudesPendientes = perfilService.obtenerPerfilesConSolicitudesPendientes(perfilId);
        return ResponseEntity.ok().body(usuariosConSolicitudesPendientes);
    }



    //Método para obtener los datos del Perfil visitado
    @GetMapping("/{username}")
    public ResponseEntity<UsuarioDTO> obtenerPerfilDeUsuario(@PathVariable String username) {
        Usuario usuario = usuarioService.obtenerUsuarioPorUsername(username);
        if (usuario != null) {
            UsuarioDTO usuarioDTO = UsuarioConverter.entityToDTO(usuario);
            return ResponseEntity.ok(usuarioDTO);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }
}