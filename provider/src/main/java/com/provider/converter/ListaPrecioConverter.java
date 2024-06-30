package com.provider.converter;

import com.provider.dto.ListaPrecioDTO;
import com.provider.dto.PrecioDTO;
import com.provider.entities.Empresa;
import com.provider.entities.ListaPrecio;

import java.util.List;
import java.util.stream.Collectors;

public class ListaPrecioConverter {

    public static ListaPrecioDTO entityToDTO(ListaPrecio listaPrecio) {
        List<PrecioDTO> preciosDTO = listaPrecio.getPrecios().stream()
                .map(PrecioConverter::entityToDTO)
                .collect(Collectors.toList());

        return new ListaPrecioDTO(listaPrecio.getId(), listaPrecio.getNombre(), preciosDTO);
    }


}