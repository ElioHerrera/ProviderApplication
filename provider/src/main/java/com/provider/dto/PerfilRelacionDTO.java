package com.provider.dto;

import lombok.*;

import java.util.List;
@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PerfilRelacionDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String fotoPerfil;
    private ComercioDTO comercio;
    private EmpresaDTO empresa;
    private List<ProductoDTO> productos;
    private UsuarioDTO usuario;
}