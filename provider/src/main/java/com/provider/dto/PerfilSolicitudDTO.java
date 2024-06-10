package com.provider.dto;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PerfilSolicitudDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String fotoPerfil;
    private String descripcion;
    private UsuarioDTO usuario;
    private ComercioDTO comercio;
    private EmpresaDTO empresa;

}