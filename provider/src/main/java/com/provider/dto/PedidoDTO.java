package com.provider.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class PedidoDTO {
    private Long id;
    private int numeroPedido;
    private Date fecha;
    private double descuento;
    private double subtotalPedido;
    private double total;
    private String estado;
    private Long clienteId;
    private Long proveedorId;
    private List<ItemDTO> items; // Lista de items
}
