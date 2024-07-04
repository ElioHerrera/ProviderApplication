package com.provider.converter;

import com.provider.dto.ListaPrecioDTO;
import com.provider.dto.ListaPrecioRelacionDTO;
import com.provider.dto.PrecioDTO;
import com.provider.entities.ListaPrecio;

import java.util.List;
import java.util.stream.Collectors;

public class ListaPrecioRelacionConverter {

    public static ListaPrecioRelacionDTO entityToDTO(ListaPrecio listaPrecio) {

        ListaPrecioRelacionDTO dto = ListaPrecioRelacionDTO.builder()
                .idLista(listaPrecio.getId())
                .idEmpresa(listaPrecio.getEmpresa().getId())
                .nombre(listaPrecio.getNombre())
                .build();

        return dto;
    }


}