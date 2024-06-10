package com.provider.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comercio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String telefono;
    private String rubro;
    private String domicilio;

    @OneToOne
    @JoinColumn(name = "perfil_id")
    @JsonIgnore // Evita la serialización de la relación
    private Perfil comerciante;

    @ManyToMany
    @JoinTable(
            name = "comercio_proveedor",
            joinColumns = @JoinColumn(name = "comercio_id"),
            inverseJoinColumns = @JoinColumn(name = "proveedor_id")
    )
    private List<Perfil> proveedores;

    @OneToMany(mappedBy = "comercio", cascade = CascadeType.ALL)
    private List<Pedido> pedidos;
}