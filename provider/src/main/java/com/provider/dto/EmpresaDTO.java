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
    private Long idEmpresa;
    private String nombre;
    private String fotoEmpresa;
    private String rubro;
    private String telefono;
    private String domicilio;
}