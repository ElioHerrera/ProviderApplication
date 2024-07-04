package com.provider.converter;

import com.provider.dto.ListaPrecioDTO;
import com.provider.dto.PrecioDTO;
import com.provider.entities.Empresa;
import com.provider.entities.ListaPrecio;

import java.util.List;
import java.util.stream.Collectors;

public class ListaPrecioConverter {

    public static ListaPrecioDTO entityToDTO(ListaPrecio listaPrecio) {

        ListaPrecioDTO dto = ListaPrecioDTO.builder()
                .idLista(listaPrecio.getId())
                .nombre(listaPrecio.getNombre())
                .build();

        if (listaPrecio != null){
            List<PrecioDTO> preciosDTO = listaPrecio.getPrecios().stream()
                    .map(PrecioConverter::entityToDTO)
                    .collect(Collectors.toList());
        }

        return dto;
    }


}