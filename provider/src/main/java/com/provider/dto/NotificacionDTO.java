package com.provider.dto;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionDTO {
    private Long id;
    private PerfilDTO destinatario;
    private String mensaje;
    private Date fecha;
    private boolean leido;

}



