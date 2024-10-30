package com.provider.controller;

import com.provider.converter.SolicitudConverter;
import com.provider.dto.SolicitudDTO;
import com.provider.entities.Perfil;
import com.provider.entities.Solicitud;
import com.provider.other.Consola;
import com.provider.services.PerfilService;
import com.provider.services.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    @Autowired
    private SolicitudService solicitudService;
    @Autowired
    private PerfilService perfilService;


    @PostMapping("enviar/{perfilUsuarioId}/{perfilProveedorId}")
    public ResponseEntity<?> enviarSolicitudAmistad(@PathVariable("perfilUsuarioId") Long perfilComercianteId, @PathVariable("perfilProveedorId") Long perfilProveedorId) {

        Consola.azul("     METODO : CLASS SolicitudController : enviarSolicitudAmistad()");
        Perfil solicitante = perfilService.obtenerPerfilPorId(perfilComercianteId).orElse(null);
        Perfil solicitado = perfilService.obtenerPerfilPorId(perfilProveedorId).orElse(null);


        if (solicitante != null && solicitado != null && solicitante != solicitado) {

            Solicitud solicitud = Solicitud.builder()
                    .solicitante(solicitante)
                    .solicitado(solicitado)
                    .fechaSolicitud(new Date())
                    .aceptada(false)
                    .pendiente(true)
                    .build();

            solicitante.getSolicitudesEnviadas().add(solicitud);
            solicitado.getSolicitudesRecibidas().add(solicitud);

            solicitudService.guardarSolicitud(solicitud);
            perfilService.guardarPerfil(solicitante);
            perfilService.guardarPerfil(solicitado);

            String mensaje = "Solicitud de amistad enviada con éxito.";
            return ResponseEntity.ok().body(Collections.singletonMap("mensaje", mensaje));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró uno o ambos perfiles.");
        }
    }

    @GetMapping("exists/{perfilUsuarioId}/{perfilProveedorId}")
    public ResponseEntity<?> verificarSolicitudExistente(@PathVariable("perfilUsuarioId") Long perfilComercianteId, @PathVariable("perfilProveedorId") Long perfilProveedorId) {

        System.out.println("     METODO : CLASS SolicitudController : verificaSolicitudExistente()");

        boolean exists = solicitudService.existeSolicitud(perfilComercianteId, perfilProveedorId);
        return ResponseEntity.ok().body(Map.of("exists", exists));
    }

    @GetMapping("/{perfilId}/solicitudes-recibidas")
    public ResponseEntity<List<SolicitudDTO>> obtenerSolicitudesRecibidas(@PathVariable("perfilId") Long perfilId) {
        Consola.azul("     METODO : CLASS SolicitudController : obtenerSolicitudesRecibidas()");

        List<Solicitud> solicitudesRecibidas = solicitudService.obtenerSolicitudesRecibidas(perfilId);
        List<SolicitudDTO> listaSolicitudesRecibidasDTO = new ArrayList<>();

        for (Solicitud solicitud : solicitudesRecibidas) {
            SolicitudDTO dto = SolicitudConverter.entityToDTO(solicitud);
            listaSolicitudesRecibidasDTO.add(dto);
        }

        return ResponseEntity.ok().body(listaSolicitudesRecibidasDTO);
    }

    @GetMapping("obtener/{solicitudId}")
    public ResponseEntity<SolicitudDTO> obtenerSolicitud(@PathVariable Long solicitudId) {
        Consola.azul("     METODO : CLASS SolicitudController : obtenerSolicitud()");
        Optional<Solicitud> solicitudOptional = solicitudService.obtenerSolicitudPorId(solicitudId);

        if (solicitudOptional.isPresent()) {
            Solicitud solicitud = solicitudOptional.get();

            SolicitudDTO solicitudDTO = SolicitudConverter.entityToDTO(solicitud);
            return ResponseEntity.ok(solicitudDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("aceptar/{solicitudId}") // Método para aceptar una solicitud
    public ResponseEntity<?> aceptarSolicitud(@PathVariable Long solicitudId) {
        Consola.azul("     METODO : CLASS SolicitudController : aceptarSolicitud()");

        boolean solicitudAceptada = solicitudService.aceptarSolicitud(solicitudId);
        if (solicitudAceptada) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("cancelar/{solicitudId}")
    public ResponseEntity<?> cancelarSolicitud(@PathVariable Long solicitudId) {


        boolean solicitudCancelada = solicitudService.cancelarSolicitud(solicitudId);
        if (solicitudCancelada) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}