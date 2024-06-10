package com.provider.dto;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductoPedidoDTO {
    private Long id;
    private Integer cantidad;
    private ProductoDTO producto;
    private PedidoDTO pedido;
}