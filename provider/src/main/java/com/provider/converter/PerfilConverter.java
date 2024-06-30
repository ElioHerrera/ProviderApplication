package com.provider.converter;

import com.provider.dto.PerfilDTO;
import com.provider.entities.Comercio;
import com.provider.entities.Empresa;
import com.provider.entities.Perfil;

import java.util.stream.Collectors;

public class PerfilConverter {

    public static PerfilDTO entityToDTO(Perfil perfil) {

        PerfilDTO dto = PerfilDTO.builder()
                .idPerfil(perfil.getId())
                .nombre(perfil.getNombre())
                .apellido(perfil.getApellido())
                .fotoPerfil(perfil.getFotoPerfil())
                .descripcion(perfil.getDescripcion())
                .build();

        if (perfil.getEmpresa() != null) {
            dto.setEmpresa(EmpresaConverter.entityToDTO(perfil.getEmpresa()));
        } else if (perfil.getComercio() != null) {
            dto.setComercio(ComercioConverter.entityToDTO(perfil.getComercio()));
        }

        return dto;
    }
}