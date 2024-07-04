package com.provider.dto;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductoProveedorDTO {

    private Long idProducto;
    private Long idUsuario;
    private int codigo;
    private String nombre;
    private String descripcion;
    private String fotoProducto;
    private double precio;
    private boolean isEnabled;

}
