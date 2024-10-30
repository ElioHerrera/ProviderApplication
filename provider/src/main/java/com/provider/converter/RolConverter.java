package com.provider.converter;

import com.provider.dto.RolDTO;
import com.provider.entities.Rol;
import java.util.stream.Collectors;

public class RolConverter {

    public static RolDTO entityToDTO(Rol rol) {

        RolDTO dto = RolDTO.builder()
                .idRol(rol.getId())
                .rol(rol.getRol().toString())
                .listaDePermisos(rol.getListaDePermisos().stream()
                        .map(permiso -> permiso.getNombrePermiso())
                        .collect(Collectors.toSet()))
                .build();
        return dto;
    }
}