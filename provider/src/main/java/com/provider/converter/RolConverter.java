package com.provider.converter;

import com.provider.dto.RolDTO;
import com.provider.entities.Rol;
import com.provider.entities.RoleEnum;

import java.util.stream.Collectors;

public class RolConverter {

    public static RolDTO entityToDTO(Rol rol) {
        RolDTO dto = new RolDTO();
        dto.setId(rol.getId());
        dto.setRoleEnum(rol.getRoleEnum().toString());
        dto.setListaDePermisos(rol.getListaDePermisos().stream()
                .map(permiso -> permiso.getNombrePermiso())
                .collect(Collectors.toSet()));
        return dto;
    }



}