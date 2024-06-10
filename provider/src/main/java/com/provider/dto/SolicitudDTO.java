package com.provider.dto;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudDTO {
    private Long id;
    private PerfilSolicitudDTO solicitante;
    private PerfilSolicitudDTO solicitado;
    private Date fechaSolicitud;
    private boolean aceptada;
}