package com.provider.dto;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComercioDTO {
    private Long idComercio;
    private String nombre;
    private String rubro;
    private String telefono;
    private String domicilio;
}