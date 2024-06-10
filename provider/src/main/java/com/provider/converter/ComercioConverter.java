package com.provider.converter;

import com.provider.dto.ComercioDTO;
import com.provider.entities.Comercio;

import java.util.stream.Collectors;

public class ComercioConverter {

    public static ComercioDTO entityToDTO(Comercio comercio) {
        ComercioDTO dto = new ComercioDTO();
        dto.setId(comercio.getId()); // Incluimos el ID para transferir al cliente
        dto.setNombre(comercio.getNombre());
        dto.setDomicilio(comercio.getDomicilio());
        dto.setRubro(comercio.getRubro());
        dto.setTelefono(comercio.getTelefono());
        //dto.setComerciante(PerfilConverter.entityToDTO(comercio.getComerciante()));
//        dto.setProveedores(comercio.getProveedores().stream()
//                .map(PerfilConverter::entityToDTO)
//                .collect(Collectors.toList()));
//        dto.setPedidos(comercio.getPedidos().stream()
//                .map(PedidoConverter::entityToDTO)
//                .collect(Collectors.toList()));
        return dto;
    }

//    public static Comercio dtoToEntity(ComercioDTO dto) {
//        Comercio comercio = new Comercio();
//        // comercio.setId(dto.getId()); // No establecer el ID si es una nueva entidad
//        comercio.setNombre(dto.getNombre());
//        comercio.setDomicilio(dto.getDomicilio());
//        comercio.setComerciante(PerfilConverter.dtoToEntity(dto.getComerciante()));
//        comercio.setProveedores(dto.getProveedores().stream()
//                .map(PerfilConverter::dtoToEntity)
//                .collect(Collectors.toList()));
//        comercio.setPedidos(dto.getPedidos().stream()
//                .map(PedidoConverter::dtoToEntity)
//                .collect(Collectors.toList()));
//        return comercio;
//    }
}