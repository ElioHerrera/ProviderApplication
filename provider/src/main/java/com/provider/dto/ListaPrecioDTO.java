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
public class ListaPrecioDTO {
    private Long id;
    private String nombre;
    private Date fecha;
    private List<ProductoDTO> productos;
    //private EmpresaDTO empresa;
}