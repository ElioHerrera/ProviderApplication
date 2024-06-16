package com.provider.controller;

import com.provider.converter.UsuarioConverter;
import com.provider.dto.UsuarioDTO;
import com.provider.entities.Usuario;
import com.provider.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("")
    public ResponseEntity<?> obtenerProveedores() {
        List<Usuario> proveedores = usuarioService.obtenerProveedores();
        List<UsuarioDTO> proveedoresDTO = new ArrayList<>();

        for (Usuario usuario : proveedores) {
            UsuarioDTO proveedorDTO = new UsuarioDTO();
            proveedorDTO = UsuarioConverter.entityToDTO(usuario);
            proveedoresDTO.add(proveedorDTO);
        }
        if (proveedores.isEmpty()) {
            return ResponseEntity.ok("No hay proveedores disponibles.");
        } else {
            return ResponseEntity.ok(proveedoresDTO);
        }
    }

}

