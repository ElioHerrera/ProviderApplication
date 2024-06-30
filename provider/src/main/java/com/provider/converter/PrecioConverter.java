package com.provider.converter;

import com.provider.dto.PrecioDTO;
import com.provider.entities.ListaPrecio;
import com.provider.entities.Precio;
import com.provider.entities.Producto;

public class PrecioConverter {

    public static PrecioDTO entityToDTO(Precio precio) {
        PrecioDTO dto = PrecioDTO.builder()
                .id(precio.getId())
                .listaId(precio.getLista().getId())
                .productoId(precio.getProducto().getId())
                .precio(precio.getPrecio())
                .build();

        return dto;
    }

}