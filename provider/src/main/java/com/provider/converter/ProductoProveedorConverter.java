package com.provider.converter;

import com.provider.dto.ProductoDTO;
import com.provider.dto.ProductoProveedorDTO;
import com.provider.entities.Producto;

public class ProductoProveedorConverter {

    public static ProductoProveedorDTO entityToDTO(Producto producto) {

        ProductoProveedorDTO dto = ProductoProveedorDTO.builder()
                .idProducto(producto.getId())
                .idUsuario(producto.getEmpresa().getProveedor().getUsuario().getId())
                .nombre(producto.getNombre())
                .codigo(producto.getCodigo())
                .isEnabled(producto.isEnabled())
                .descripcion(producto.getDescripcion())
                .fotoProducto(producto.getFotoProducto())
                .build();


        return dto;
    }


}
