package com.provider.converter;

import com.provider.dto.SolicitudDTO;
import com.provider.entities.Solicitud;

public class SolicitudConverter {

    public static SolicitudDTO entityToDTO(Solicitud solicitud) {

        SolicitudDTO dto = SolicitudDTO.builder()
                .idSolicitud(solicitud.getId())
                .solicitante(PerfilRelacionConverter.entityToDTO(solicitud.getSolicitante()))
                .solicitado(PerfilRelacionConverter.entityToDTO(solicitud.getSolicitado()))
                .fechaSolicitud(solicitud.getFechaSolicitud())
                .aceptada(solicitud.isAceptada())
                .build();

        return dto;
    }

}
