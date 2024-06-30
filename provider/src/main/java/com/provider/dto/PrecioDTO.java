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
    private Long id;
    private Long productoId;
    private Long listaId;
    private double precio;
}