package com.provider.dto;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublicacionDTO {
    private Long idPublicacion;
    private PerfilRelacionDTO autor;
    private String contenido;
    private String fotoPublicacion;
    private Date fecha;
}