package com.provider.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
            name = "empresa_comercio",
            joinColumns = @JoinColumn(name = "comercio_id"),
            inverseJoinColumns = @JoinColumn(name = "empresa_id")
    )
    private List<Empresa> proveedores;



    @ManyToMany
    @JoinTable(
            name = "comercio_lista_precios",
            joinColumns = @JoinColumn(name = "comercio_id"),
            inverseJoinColumns = @JoinColumn(name = "lista_id")
    )
    private List<ListaPrecio> listasAsignadas;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pedido> pedidos = new ArrayList<>(); // Relación con los pedidos realizados
}