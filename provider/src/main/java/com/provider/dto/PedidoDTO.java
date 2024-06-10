package com.provider.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDTO {
    private Long id;
    private Date fecha;
    private String estado;
    private List<ProductoPedidoDTO> productos;
    private ComercioDTO comercio;


}