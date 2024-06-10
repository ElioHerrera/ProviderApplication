package com.provider.converter;

import com.provider.dto.UsuarioDTO;
import com.provider.entities.Usuario;

import java.util.stream.Collectors;

public class UsuarioConverter {

    public static UsuarioDTO entityToDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setEmail(usuario.getEmail());
        dto.setUsername(usuario.getUsername());
        dto.setTipoUsuario(usuario.getTipoUsuario());
        dto.setEnabled(usuario.isEnabled());
        dto.setPerfil(PerfilConverter.entityToDTO(usuario.getPerfil()));
        dto.setRoles(usuario.getRoles().stream()
                .map(RolConverter::entityToDTO)
                .collect(Collectors.toSet()));
        return dto;
    }

}