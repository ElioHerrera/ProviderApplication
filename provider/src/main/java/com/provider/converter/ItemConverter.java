package com.provider.converter;

import com.provider.dto.ItemDTO;
import com.provider.dto.ProductoDTO;
import com.provider.entities.Item;
import com.provider.entities.Pedido;
import com.provider.entities.Producto;
import jakarta.persistence.*;
import org.springframework.stereotype.Component;

@Component
public class ItemConverter {

    public static ItemDTO entityToDTO(Item item) {

        ProductoConverter convertidor = new ProductoConverter();

        return ItemDTO.builder()
                .id(item.getId())
                .pedidoId(item.getPedido().getId())
                .producto(convertidor.entityToDTO(item.getProducto()))
                .cantidad(item.getCantidad())
                .precioUnitario(item.getPrecioUnitario())
                .subtotal(item.getSubtotal())
                .build();
    }

    public static Item dtoToEntity(ItemDTO dto, Pedido pedido) {
        Item item = new Item();
        item.setPedido(pedido);
        //item.setProducto(dto.getProducto());
        item.setCantidad(dto.getCantidad());
        item.setPrecioUnitario(dto.getPrecioUnitario());
        item.setSubtotal(dto.getSubtotal());
        return item;
    }


}

//public class ItemDTO {
//    private Long id;
//    private Long pedidoId;
//    private ProductoDTO producto;
//    private int cantidad;
//    private double precioUnitario;
//    private double subtotal;
//}

//public class Item {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "pedido_id")
//    private Pedido pedido;
//
//    @ManyToOne
//    @JoinColumn(name = "producto_id")
//    private Producto producto;
//
//    private int cantidad;
//    private double precioUnitario;
//    private double subtotal;
//}
