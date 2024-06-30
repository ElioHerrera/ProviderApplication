package com.provider.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String rubro;
    private String fotoEmpresa;
    private String telefono;
    private String domicilio;

    @OneToOne
    @JoinColumn(name = "perfil_id")
    @JsonIgnore // Evita la serialización de la relación
    private Perfil proveedor;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<Producto> productos = new ArrayList<>();

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<ListaPrecio> listasPrecios = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "empresa_comercio",
            joinColumns = @JoinColumn(name = "empresa_id"),
            inverseJoinColumns = @JoinColumn(name = "comercio_id")
    )
    @JsonIgnore // Evita la serialización de la relación
    private List<Comercio> clientes;
}

