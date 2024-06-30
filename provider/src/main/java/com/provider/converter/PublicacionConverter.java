package com.provider.converter;

import com.provider.dto.PublicacionDTO;
import com.provider.entities.Publicacion;

public class PublicacionConverter {

    public static PublicacionDTO entityToDTO(Publicacion publicacion) {

        PublicacionDTO dto = PublicacionDTO.builder()
                .idPublicacion(publicacion.getId())
                .autor(PerfilRelacionConverter.entityToDTO(publicacion.getAutor()))
                .fotoPublicacion(publicacion.getFotoPublicacion())
                .contenido(publicacion.getContenido())
                .fecha(publicacion.getFecha())
                .build();
        return dto;
    }
}
