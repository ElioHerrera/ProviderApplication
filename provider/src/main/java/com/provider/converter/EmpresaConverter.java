package com.provider.converter;

import com.provider.dto.EmpresaDTO;
import com.provider.dto.ListaPrecioDTO;
import com.provider.dto.PrecioDTO;
import com.provider.entities.Empresa;

import java.util.List;
import java.util.stream.Collectors;

public class EmpresaConverter {

    public static EmpresaDTO entityToDTO(Empresa empresa) {

        EmpresaDTO dto = EmpresaDTO.builder()
                .idEmpresa(empresa.getId())
                .nombre(empresa.getNombre())
                .fotoEmpresa(empresa.getFotoEmpresa())
                .domicilio(empresa.getDomicilio())
                .rubro(empresa.getRubro())
                .telefono(empresa.getTelefono())
                .build();


        if (empresa.getListasPrecios() != null){

            List<ListaPrecioDTO> listaDTO = empresa.getListasPrecios().stream()
                    .map(ListaPrecioConverter::entityToDTO)
                    .collect(Collectors.toList());

            dto.setListasDePrecio(listaDTO);
        }
        return dto;
    }

}