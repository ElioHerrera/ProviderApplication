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
    private Long idSolicitud;
    private PerfilRelacionDTO solicitante;
    private PerfilRelacionDTO solicitado;
    private Date fechaSolicitud;
    private boolean aceptada;
    private boolean pendiente;
}