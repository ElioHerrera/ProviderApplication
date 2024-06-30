package com.provider.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Publicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contenido;
    private String fotoPublicacion;
    private int likes;
    private Date fecha;

    @ManyToOne
    @JoinColumn(name = "perfil_id")
    @JsonIgnore
    private Perfil autor;

    public void darLike(){
        this.likes ++;

    }
}

