package com.provider.dto;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmpresaDTO {
    private Long id;
    private String nombre;
    private String rubro;
    private String telefono;
    private String domicilio;
    //private PerfilDTO proveedor;
    private List<ProductoDTO> productos;
    private List<ListaPrecioDTO> listasPrecios;
}