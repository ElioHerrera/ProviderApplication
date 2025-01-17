package com.provider.dto;

import lombok.*;

import java.util.Set;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RolDTO {
    private Long idRol;
    private String rol;
    private Set<String> listaDePermisos;
}