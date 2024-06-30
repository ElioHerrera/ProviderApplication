package com.provider.converter;

import com.provider.dto.ProductoDTO;
import com.provider.entities.Precio;
import com.provider.entities.Producto;
import com.provider.repositories.PrecioRepository;
import com.provider.services.PrecioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ProductoConverter {

    @Autowired
    private PrecioService precioService;

    public ProductoDTO entityToDTO(Producto producto){



        ProductoDTO dto = ProductoDTO.builder()
                .idProducto(producto.getId())
                .idUsuario(producto.getEmpresa().getProveedor().getUsuario().getId())
                .nombre(producto.getNombre())
                .codigo(producto.getCodigo())
                .isEnabled(producto.isEnabled())
                .descripcion(producto.getDescripcion())
                .fotoProducto(producto.getFotoProducto())
                .build();

        List<Precio> precios = precioService.obtenerPreciosPorProducto(producto.getId());
        double lista1=0.0, lista2=0, lista3=0;


        for (Precio precio : precios) {
            if (precio.getLista().getNombre().equals("Lista 1")) {
                lista1 = precio.getPrecio();
            } else if (precio.getLista().getNombre().equals("Lista 2")) {
                lista2 = precio.getPrecio();
            } else if (precio.getLista().getNombre().equals("Lista 3")) {
                lista3 = precio.getPrecio();
            }
        }

        dto.setLista1(lista1);
        dto.setLista2(lista2);
        dto.setLista3(lista3);

        return dto;
    }
}
