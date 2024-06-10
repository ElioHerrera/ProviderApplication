package com.provider.converter;

import com.provider.dto.PerfilDTO;
import com.provider.entities.Perfil;

import java.util.stream.Collectors;


public class PerfilConverter {

    public static PerfilDTO entityToDTO(Perfil perfil) {
        PerfilDTO dto = new PerfilDTO();
        dto.setId(perfil.getId());
        dto.setNombre(perfil.getNombre());
        dto.setApellido(perfil.getApellido());
        dto.setFotoPerfil(perfil.getFotoPerfil());
        dto.setDescripcion(perfil.getDescripcion());



        if (perfil.getEmpresa() != null) {
            dto.setEmpresa(EmpresaConverter.entityToDTO(perfil.getEmpresa()));
        } else if (perfil.getComercio() != null) {
            dto.setComercio(ComercioConverter.entityToDTO(perfil.getComercio()));
        }
        return dto;
    }

    /*if (perfil.getRelacioneComerciales() != null) {dto.setRelacionesComerciales(perfil.getRelacioneComerciales().stream().map(PerfilConverter::entityToDTO).collect(Collectors.toList()));}
    if (perfil.getSolicitudesEnviadas() != null) {dto.setSolicitudesEnviadas(perfil.getSolicitudesEnviadas().stream().map(SolicitudConverter::entityToDTO).collect(Collectors.toList()));}
    if (perfil.getSolicitudesRecibidas() != null) {dto.setSolicitudesRecibidas(perfil.getSolicitudesRecibidas().stream().map(SolicitudConverter::entityToDTO).collect(Collectors.toList()));}*/

}