package com.provider.converter;

import com.provider.dto.EmpresaDTO;
import com.provider.entities.Empresa;

import java.util.stream.Collectors;

public class EmpresaConverter {

    public static EmpresaDTO entityToDTO(Empresa empresa) {

        EmpresaDTO dto = new EmpresaDTO();
        dto.setId(empresa.getId()); // Incluimos el ID para transferir al cliente
        dto.setNombre(empresa.getNombre());
        dto.setDomicilio(empresa.getDomicilio());
        dto.setRubro(empresa.getRubro());
        dto.setTelefono(empresa.getTelefono());

        return dto;
    }

    /*dto.setProveedor(PerfilConverter.entityToDTO(empresa.getProveedor()));
    dto.setProductos(empresa.getProductos().stream().map(ProductoConverter::entityToDTO).collect(Collectors.toList()));
    dto.setListasPrecios(empresa.getListasPrecios().stream().map(ListaPrecioConverter::entityToDTO).collect(Collectors.toList()));*/

}