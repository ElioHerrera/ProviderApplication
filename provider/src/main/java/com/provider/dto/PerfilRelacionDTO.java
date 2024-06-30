package com.provider.dto;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PerfilRelacionDTO {
    private Long id;
    private Long idPerfil;
    private String username;
    private String email;
    private String nombre;
    private String apellido;
    private String fotoPerfil;
    private ComercioDTO comercio;
    private EmpresaDTO empresa;

}