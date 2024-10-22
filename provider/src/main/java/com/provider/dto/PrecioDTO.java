package com.provider.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrecioDTO {
    private Long idPrecio;
    private Long productoId;
    private Long listaId;
    private double precio;
}