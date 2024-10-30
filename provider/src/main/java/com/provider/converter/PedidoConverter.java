package com.provider.converter;

import com.provider.dto.PedidoDTO;
import com.provider.entities.EstadoPedido;
import com.provider.entities.Pedido;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PedidoConverter {

    public static PedidoDTO entityToDTO(Pedido pedido) {
        PedidoDTO dto = PedidoDTO.builder()
                .id(pedido.getId())
                .numeroPedido(pedido.getNumeroPedido())
                .fecha(pedido.getFecha())
                .descuento(pedido.getDescuento())
                .subtotalPedido(pedido.getSubTotal())
                .total(pedido.getTotal())
                .estado(pedido.getEstado().name())
                .clienteId(pedido.getCliente().getId())
                .proveedorId(pedido.getProveedor().getId())
                .items(pedido.getItems().stream().map(ItemConverter::entityToDTO).collect(Collectors.toList()))
                .build();

        return dto;
    }

    public static Pedido dtoToEntity(PedidoDTO dto) {
        Pedido pedido = new Pedido();
        pedido.setNumeroPedido(dto.getNumeroPedido());
        pedido.setFecha(dto.getFecha());
        pedido.setDescuento(dto.getDescuento());
        pedido.setSubTotal(dto.getSubtotalPedido());
        pedido.setTotal(dto.getTotal());
        pedido.setEstado(EstadoPedido.valueOf(dto.getEstado()));
        // Asigna cliente y proveedor desde el servicio correspondiente
        return pedido;
    }
}
