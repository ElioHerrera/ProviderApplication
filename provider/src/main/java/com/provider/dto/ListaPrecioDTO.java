package com.provider.dto;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListaPrecioDTO {
    private Long idLista;
    private String nombre;
    private List<PrecioDTO> precios;
}
