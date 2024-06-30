package com.provider.converter;

import com.provider.dto.ComercioDTO;
import com.provider.entities.Comercio;

import java.util.stream.Collectors;

public class ComercioConverter {

    public static ComercioDTO entityToDTO(Comercio comercio) {

        ComercioDTO dto = ComercioDTO.builder()
                .idComercio(comercio.getId())
                .nombre(comercio.getNombre())
                .domicilio(comercio.getDomicilio())
                .rubro(comercio.getRubro())
                .telefono(comercio.getTelefono())
                .build();

        return dto;
    }
}