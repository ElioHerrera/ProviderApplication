package com.provider.controller;

import com.provider.converter.PerfilRelacionConverter;
import com.provider.dto.PerfilRelacionDTO;
import com.provider.entities.Perfil;
import com.provider.entities.Usuario;
import com.provider.services.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    @Autowired
    private PerfilService perfilService;


    // Método para buscar proveedores según un término de búsqueda
    @GetMapping("/buscar")
    public ResponseEntity<List<Perfil>> buscarProveedores(@RequestParam String terminoBusqueda) {
        List<Perfil> proveedores = perfilService.obtenerProveedoresBuscados(terminoBusqueda);
        return ResponseEntity.ok(proveedores);
    }
}