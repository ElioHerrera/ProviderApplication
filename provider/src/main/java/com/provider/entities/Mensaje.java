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
public class Mensaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "emisor_id")
    private Perfil emisor;

    @ManyToOne
    @JoinColumn(name = "receptor_id")
    private Perfil receptor;

    private String contenido;
    private Date fechaEnvio;
    private boolean leido;
}