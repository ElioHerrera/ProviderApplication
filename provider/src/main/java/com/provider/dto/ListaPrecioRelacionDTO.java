package com.provider.dto;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListaPrecioRelacionDTO {
    private Long idLista;
    private Long idEmpresa;
    private String nombre;

}
