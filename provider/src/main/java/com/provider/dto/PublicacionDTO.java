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
    private Long id;
    private String contenido;
    private Date fecha;
    private PerfilDTO autor;
}