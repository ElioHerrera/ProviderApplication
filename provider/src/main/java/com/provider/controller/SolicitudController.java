package com.provider.controller;

import com.provider.converter.UsuarioConverter;
import com.provider.dto.PerfilSolicitudDTO;
import com.provider.dto.SolicitudDTO;
import com.provider.entities.Perfil;
import com.provider.services.PerfilService;
import com.provider.services.SolicitudService;
import com.provider.entities.Solicitud;
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

        // Obtener los perfiles del usuario y del proveedor utilizando los IDs
        Perfil solicitante = perfilService.findById(perfilComercianteId).orElse(null);
        Perfil solicitado = perfilService.findById(perfilProveedorId).orElse(null);

        System.out.println("Enviar Solicitud: -Solicitante: " + solicitante.getNombre() + solicitante.getUsuario().getEmail() + " -Proveedor: " + solicitado.getNombre() + solicitado.getUsuario().getEmail());

        // Verificar si los perfiles existen
        if (solicitante != null && solicitado != null) {
            // Crear la solicitud
            Solicitud solicitud = new Solicitud();
            solicitud.setSolicitante(solicitante);
            solicitud.setSolicitado(solicitado);
            solicitud.setFechaSolicitud(new Date());
            solicitud.setAceptada(false);


            System.out.println("Guardar solicitud");

            // Guardar la solicitud en la base de datos
            solicitudService.guardarSolicitud(solicitud);

            System.out.println("Agregar y guardar solicitud al solicitante");
            //Agregar la solicitud a la lista de solicitudes enviadas del solicitante
            solicitante.getSolicitudesEnviadas().add(solicitud);
            perfilService.save(solicitante);
            System.out.println("Agregar y guardar solicitud al solicitado");
            // Agregar la solicitud a la lista de solicitudes recibidas del solicitado
            solicitado.getSolicitudesRecibidas().add(solicitud);
            perfilService.save(solicitado);

            String mensaje = "Solicitud de amistad enviada con éxito.";
            return ResponseEntity.ok().body(Collections.singletonMap("mensaje", mensaje));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró uno o ambos perfiles.");
        }
    }

    @GetMapping("exists/{perfilUsuarioId}/{perfilProveedorId}")
    public ResponseEntity<?> verificarSolicitudExistente(@PathVariable("perfilUsuarioId") Long perfilComercianteId, @PathVariable("perfilProveedorId") Long perfilProveedorId) {

        System.out.println("BUSCAR SOLICITUD SI EXISTE Id perfiles:" + perfilComercianteId.toString() + "  " + perfilProveedorId.toString());
        boolean exists = solicitudService.existeSolicitud(perfilComercianteId, perfilProveedorId);
        return ResponseEntity.ok().body(Map.of("exists", exists));
    }

    @GetMapping("/{perfilId}/solicitudes-recibidas")
    public ResponseEntity<List<SolicitudDTO>> obtenerSolicitudesRecibidas(@PathVariable("perfilId") Long perfilId) {


        List<Solicitud> solicitudesRecibidas = perfilService.obtenerSolicitudesRecibidas(perfilId);

        List<SolicitudDTO> solicitudDTOList = new ArrayList<>();

        for (Solicitud solicitud : solicitudesRecibidas) {
            SolicitudDTO solicitudDTO = new SolicitudDTO();
            solicitudDTO.setId(solicitud.getId());
            solicitudDTO.setFechaSolicitud(solicitud.getFechaSolicitud());
            solicitudDTO.setAceptada(solicitud.isAceptada());

            // Mapear el perfil del solicitante a un PerfilSolicitudDTO
            PerfilSolicitudDTO solicitanteDTO = new PerfilSolicitudDTO();
            solicitanteDTO.setId(solicitud.getSolicitante().getId());
            solicitanteDTO.setNombre(solicitud.getSolicitante().getNombre());
            solicitanteDTO.setApellido(solicitud.getSolicitante().getApellido());
            solicitanteDTO.setFotoPerfil(solicitud.getSolicitante().getFotoPerfil());
            //solicitanteDTO.setUsuario(solicitud.getSolicitante().getUsuario());
            solicitanteDTO.setUsuario(UsuarioConverter.entityToDTO(solicitud.getSolicitante().getUsuario())); // Convertir y asignar UsuarioDTO
            // Otros campos del perfil del solicitante...
            solicitudDTO.setSolicitante(solicitanteDTO);

            // Mapear el perfil del solicitado a un PerfilSolicitudDTO
            PerfilSolicitudDTO solicitadoDTO = new PerfilSolicitudDTO();
            solicitadoDTO.setId(solicitud.getSolicitado().getId());
            solicitadoDTO.setNombre(solicitud.getSolicitado().getNombre());
            solicitadoDTO.setApellido(solicitud.getSolicitado().getApellido());
            solicitadoDTO.setFotoPerfil(solicitud.getSolicitado().getFotoPerfil());
            solicitadoDTO.setUsuario(UsuarioConverter.entityToDTO(solicitud.getSolicitado().getUsuario()));
            // Otros campos del perfil del solicitado...
            solicitudDTO.setSolicitado(solicitadoDTO);


            solicitudDTOList.add(solicitudDTO);
        }


        return ResponseEntity.ok().body(solicitudDTOList);
    }

    @GetMapping("obtener/{solicitudId}")
    public ResponseEntity<SolicitudDTO> obtenerSolicitud(@PathVariable Long solicitudId) {
        Optional<Solicitud> solicitudOptional = solicitudService.obtenerSolicitudPorId(solicitudId);

        if (solicitudOptional.isPresent()) {
            Solicitud solicitud = solicitudOptional.get();

            SolicitudDTO solicitudDTO = SolicitudDTO.builder()
                    .id(solicitud.getId())
                    .fechaSolicitud(solicitud.getFechaSolicitud())
                    .aceptada(solicitud.isAceptada())
                    .build();

            return ResponseEntity.ok(solicitudDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("aceptar/{solicitudId}") // Método para aceptar una solicitud
    public ResponseEntity<?> aceptarSolicitud(@PathVariable Long solicitudId) {

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