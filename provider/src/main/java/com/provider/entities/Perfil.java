package com.provider.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Perfil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String fotoPerfil;
    private String descripcion;

    @OneToOne
    @JoinColumn(name = "usuario_id", updatable = false, nullable = false)
    @JsonIgnore
    private Usuario usuario;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Publicacion> publicaciones;

    @ManyToMany
    @JoinTable(name = "relaciones_comerciales", joinColumns = @JoinColumn(name = "perfil_id"), inverseJoinColumns = @JoinColumn(name = "asociado_id"))
    @JsonIgnore
    private Set<Perfil> relacioneComerciales;

    @OneToOne(mappedBy = "comerciante", cascade = CascadeType.ALL)
    private Comercio comercio;

    @OneToOne(mappedBy = "proveedor", cascade = CascadeType.ALL)
    private Empresa empresa;

    @OneToMany(mappedBy = "receptor", cascade = CascadeType.ALL)
    private List<Mensaje> mensajesRecibidos;

    @OneToMany(mappedBy = "emisor", cascade = CascadeType.ALL)
    private List<Mensaje> mensajesEnviados;

    @OneToMany(mappedBy = "destinatario", cascade = CascadeType.ALL)
    private List<Notificacion> notificaciones;

    @JsonIgnore
    @OneToMany(mappedBy = "solicitante", cascade = CascadeType.ALL)
    private List<Solicitud> solicitudesEnviadas;

    @JsonIgnore
    @OneToMany(mappedBy = "solicitado", cascade = CascadeType.ALL)
    private List<Solicitud> solicitudesRecibidas;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Perfil perfil = (Perfil) o;
        return Objects.equals(id, perfil.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }



}