package com.provider.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemDTO {
    private Long id;
    private Long pedidoId;
    private ProductoDTO producto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;
}




