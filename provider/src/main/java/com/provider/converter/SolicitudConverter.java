package com.provider.converter;

import com.provider.dto.SolicitudDTO;
import com.provider.entities.Solicitud;

public class SolicitudConverter {

    public static SolicitudDTO entityToDTO(Solicitud solicitud) {


        SolicitudDTO dto = new SolicitudDTO();
        dto.setId(solicitud.getId()); // Incluimos el ID para transferir al cliente
        //dto.setSolicitante(PerfilConverter.entityToDTO(solicitud.getSolicitante()));
        //dto.setSolicitado(PerfilConverter.entityToDTO(solicitud.getSolicitado()));
        dto.setFechaSolicitud(solicitud.getFechaSolicitud());
        dto.setAceptada(solicitud.isAceptada());
        return dto;
    }

}