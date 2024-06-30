package com.provider.services;

import com.provider.entities.Perfil;
import com.provider.entities.Solicitud;
import com.provider.repositories.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SolicitudService {

    @Autowired
    private SolicitudRepository solicitudRepository;
    @Autowired
    private PerfilService perfilService;
    public boolean existeSolicitud(Long solicitanteId, Long solicitadoId) {

        Solicitud solicitud = solicitudRepository.findBySolicitanteIdAndSolicitadoId(solicitanteId, solicitadoId);
        return solicitud != null;  // Verificar si se encontró una solicitud
    }
    public boolean aceptarSolicitud(Long solicitudId) {

        System.out.print("     METODO : CLASS SolicitudController : aceptarSolicitud()");
        Optional<Solicitud> optionalSolicitud = solicitudRepository.findById(solicitudId);
        if (optionalSolicitud.isPresent()) {
            Solicitud solicitud = optionalSolicitud.get();
            solicitud.setAceptada(true);

            Perfil perfilSolicitado = solicitud.getSolicitado();
            Perfil perfilSolicitante = solicitud.getSolicitante();
            perfilSolicitante.getRelacioneComerciales().add(perfilSolicitado);
            perfilSolicitado.getRelacioneComerciales().add(perfilSolicitante);

            perfilService.guardarPerfil(perfilSolicitante);
            perfilService.guardarPerfil(perfilSolicitado);
            solicitudRepository.save(solicitud);

            System.out.println("Relacion Comecial establecida entre " + perfilSolicitado.getUsuario().getUsername() + " y " + perfilSolicitante.getUsuario().getUsername());

            return true; // La solicitud se aceptó correctamente
        } else {
            return false; // No se encontró la solicitud con el ID proporcionado
        }
    }


    public boolean cancelarSolicitud(Long solicitudId) {

        System.out.print("     METODO : CLASS SolicitudController : cancelarSolicitud()");

        Optional<Solicitud> optionalSolicitud = solicitudRepository.findById(solicitudId);
        if (optionalSolicitud.isPresent()) {
            Solicitud solicitud = optionalSolicitud.get();
            solicitud.setAceptada(false);

            Perfil perfilSolicitante = solicitud.getSolicitante();
            Perfil perfilSolicitado = solicitud.getSolicitado();
            perfilSolicitante.getRelacioneComerciales().remove(perfilSolicitado);
            perfilSolicitado.getRelacioneComerciales().remove(perfilSolicitante);

            perfilService.guardarPerfil(perfilSolicitante);
            perfilService.guardarPerfil(perfilSolicitado);
            solicitudRepository.save(solicitud);

            System.out.println(" Cancelacion de Relacion Comecial entre " + perfilSolicitado.getUsuario().getUsername() + " y " + perfilSolicitante.getUsuario().getUsername());

            return true; // La solicitud se actualizó correctamente
        } else {
            return false; // No se encontró la solicitud con el ID proporcionado
        }
    }
    public Optional<Solicitud> obtenerSolicitudPorId(Long id) {
        return solicitudRepository.findById(id);
    }
    public Solicitud guardarSolicitud(Solicitud solicitud) {
        return solicitudRepository.save(solicitud);
    }
    public void eliminarSolicitudPorId(Long id) {
        solicitudRepository.deleteById(id);
    }
    public List<Solicitud> obtenerSolicitudes() {
        return solicitudRepository.findAll();
    }
}