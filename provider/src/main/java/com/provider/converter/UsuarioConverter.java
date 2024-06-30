package com.provider.converter;

import com.provider.dto.UsuarioDTO;
import com.provider.entities.Usuario;

import java.util.stream.Collectors;

public class UsuarioConverter {

    public static UsuarioDTO entityToDTO(Usuario usuario) {
        UsuarioDTO dto = UsuarioDTO.builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .username(usuario.getUsername())
                .tipoUsuario(usuario.getTipoUsuario())
                .isEnabled(usuario.isEnabled())
                .perfil(PerfilConverter.entityToDTO(usuario.getPerfil()))
                .roles(usuario.getRoles().stream().map(RolConverter::entityToDTO).collect(Collectors.toSet()))
                .build();
        return dto;
    }

}