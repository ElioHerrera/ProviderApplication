package com.provider.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "solicitante_id")
    private Perfil solicitante;

    @ManyToOne
    @JoinColumn(name = "solicitado_id")
    private Perfil solicitado;

    private Date fechaSolicitud;
    private boolean aceptada;
    private boolean pendiente;

}