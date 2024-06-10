package com.provider.dto;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PerfilDTO {
    //Evitamos pasar los datos del Usuario

    private Long id;
    private String nombre;
    private String apellido;
    private String fotoPerfil;
    private String descripcion;
    private List<PerfilDTO> relacionesComerciales;
    private ComercioDTO comercio;
    private EmpresaDTO empresa;
    /*
    private List<PublicacionDTO> publicaciones;
    private List<MensajeDTO> mensajesRecibidos;
    private List<MensajeDTO> mensajesEnviados;
    private List<NotificacionDTO> notificaciones;
    private List<SolicitudDTO> solicitudesEnviadas;
    private List<SolicitudDTO> solicitudesRecibidas;
    */
}
