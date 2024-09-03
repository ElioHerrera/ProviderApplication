package com.provider.converter;

import com.provider.dto.*;
import com.provider.entities.Perfil;
import com.provider.entities.Producto;
import java.util.ArrayList;
import java.util.List;

public class PerfilRelacionConverter {

    public static PerfilRelacionDTO entityToDTO(Perfil perfil) {

        PerfilRelacionDTO dto = PerfilRelacionDTO.builder()
                .id(perfil.getUsuario().getId())
                .idPerfil(perfil.getId())
                .username(perfil.getUsuario().getUsername())
                .email(perfil.getUsuario().getEmail())
                .nombre(perfil.getNombre())
                .apellido(perfil.getApellido())
                .fotoPerfil(perfil.getFotoPerfil())
                .build();

        if(perfil.getEmpresa() != null){
            dto.setEmpresa(EmpresaConverter.entityToDTO(perfil.getEmpresa()));
        } else if (perfil.getComercio() != null){
            dto.setComercio(ComercioConverter.entityToDTO(perfil.getComercio()));
        }
        return dto;

    }
}
