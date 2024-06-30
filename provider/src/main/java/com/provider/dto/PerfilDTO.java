package com.provider.dto;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PerfilDTO {
    private Long idPerfil;
    private String nombre;
    private String apellido;
    private String fotoPerfil;
    private String descripcion;
    private ComercioDTO comercio;
    private EmpresaDTO empresa;
}
