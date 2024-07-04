package com.provider.converter;

import com.provider.dto.ComercioDTO;
import com.provider.dto.ListaPrecioDTO;
import com.provider.dto.ListaPrecioRelacionDTO;
import com.provider.entities.Comercio;

import java.util.List;
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


        if (comercio.getListasAsignadas() != null) {
            List<ListaPrecioRelacionDTO> listaDTO = comercio.getListasAsignadas().stream()
                            .map(ListaPrecioRelacionConverter::entityToDTO)
                                    .collect(Collectors.toList());



            dto.setListasAsignada(listaDTO);
        }


        return dto;
    }
}