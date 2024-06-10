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
    @JsonIgnore // Evita la serialización de la relación
    private Usuario usuario;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL)
    private List<Publicacion> publicaciones;

    @ManyToMany
    @JoinTable(name = "relaciones_comerciales",
            joinColumns = @JoinColumn(name = "perfil_id"),
            inverseJoinColumns = @JoinColumn(name = "asociado_id"))
    private List<Perfil> relacioneComerciales;

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

    @OneToMany(mappedBy = "solicitante", cascade = CascadeType.ALL)
    private List<Solicitud> solicitudesEnviadas;

    @OneToMany(mappedBy = "solicitado", cascade = CascadeType.ALL)
    private List<Solicitud> solicitudesRecibidas;
}