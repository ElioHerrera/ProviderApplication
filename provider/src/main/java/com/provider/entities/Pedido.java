package com.provider.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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
    //private String estado;
    private double descuento;
    private double subTotal;
    private double total;

    @ManyToOne
    @JoinColumn(name = "comercio_id")
    private Comercio comercio;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<Item> items;




}
