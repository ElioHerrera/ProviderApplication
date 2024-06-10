package com.provider.converter;

import com.provider.dto.PermisoDTO;
import com.provider.entities.Permiso;

public class PermisoConverter {

    public static PermisoDTO entityToDTO(Permiso permiso) {
        PermisoDTO dto = new PermisoDTO();
        dto.setId(permiso.getId());
        dto.setNombrePermiso(permiso.getNombrePermiso());
        return dto;
    }
}