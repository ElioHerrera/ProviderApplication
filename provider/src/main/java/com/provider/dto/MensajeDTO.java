package com.provider.dto;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MensajeDTO {
    private Long id;
    private PerfilDTO emisor;
    private PerfilDTO receptor;
    private String contenido;
    private Date fechaEnvio;
    private boolean leido;
}