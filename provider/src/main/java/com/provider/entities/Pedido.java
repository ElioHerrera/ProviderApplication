package com.provider.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numeroPedido;
    private Date fecha;

    private double descuento;
    private double subTotal;
    private double total;

    @ManyToOne
    @JoinColumn(name = "comercio_id", nullable = false)
    private Comercio cliente; // Comercio que realiza el pedido

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa proveedor; // Empresa que recibe el pedido

    @Enumerated(EnumType.STRING)
    private EstadoPedido estado;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Item> items = new ArrayList<>();


}
