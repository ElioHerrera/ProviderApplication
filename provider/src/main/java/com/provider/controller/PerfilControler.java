package com.provider.controller;

import com.provider.dto.PerfilDTO;
import com.provider.dto.PerfilRelacionDTO;
import com.provider.entities.Perfil;
import com.provider.services.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/perfiles")
public class PerfilControler {

    @Autowired
    private PerfilService perfilService;

    // Otros métodos...

    @GetMapping("/{perfilId}/relaciones-comerciales")
    public ResponseEntity<List<PerfilRelacionDTO>> obtenerRelacionesComerciales(@PathVariable("perfilId") Long perfilId) {
        List<PerfilRelacionDTO> relacionesComerciales = perfilService.obtenerRelacionesComerciales(perfilId);
        return ResponseEntity.ok().body(relacionesComerciales);
    }
}