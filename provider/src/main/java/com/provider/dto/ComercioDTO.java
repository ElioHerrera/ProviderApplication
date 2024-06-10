package com.provider.dto;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComercioDTO {
    private Long id;

     private String nombre;
    private String rubro;
    private String telefono;
    private String domicilio;

    //private PerfilDTO comerciante;
    private List<PerfilDTO> proveedores;
    private List<PedidoDTO> pedidos;
}