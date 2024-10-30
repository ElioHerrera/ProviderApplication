package com.provider.services;

import com.provider.entities.Comercio;
import com.provider.entities.Empresa;
import com.provider.entities.Perfil;
import com.provider.entities.Solicitud;
import com.provider.other.Consola;
import com.provider.repositories.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SolicitudService {

    @Autowired
    private SolicitudRepository solicitudRepository;
    @Autowired
    private PerfilService perfilService;
    @Autowired
    private ComercioService comercioService;
    @Autowired
    private EmpresaService empresaService;


    public List<Solicitud> obtenerSolicitudesRecibidas(Long perfilId) {
        Perfil perfil = perfilService.obtenerPerfilPorId(perfilId).orElse(null);
        if (perfil != null) {
            return perfil.getSolicitudesRecibidas();
        } else {
            return null;
        }
    }

    public boolean existeSolicitud(Long solicitanteId, Long solicitadoId) {
        Solicitud solicitud = solicitudRepository.findBySolicitanteIdAndSolicitadoId(solicitanteId, solicitadoId);
        return solicitud != null;  // Verificar si se encontró una solicitud
    }

    @Transactional
    public boolean aceptarSolicitud(Long solicitudId) {

        Optional<Solicitud> optionalSolicitud = solicitudRepository.findById(solicitudId);
        if (optionalSolicitud.isPresent()) {
            Solicitud solicitud = optionalSolicitud.get();
            solicitud.setAceptada(true);
            solicitud.setPendiente(false);

            Perfil perfilSolicitado = solicitud.getSolicitado();
            Perfil perfilSolicitante = solicitud.getSolicitante();
            perfilSolicitante.getRelacioneComerciales().add(perfilSolicitado);
            perfilSolicitado.getRelacioneComerciales().add(perfilSolicitante);

            Comercio comercio = perfilSolicitante.getComercio();
            Empresa empresa = perfilSolicitado.getEmpresa();

            comercio.getProveedores().add(empresa);
            empresa.getClientes().add(comercio);

            //Persistir entidades
            empresaService.guardarEmpresa(empresa);
            comercioService.guardarComercio(comercio);
            perfilService.guardarPerfil(perfilSolicitante);
            perfilService.guardarPerfil(perfilSolicitado);
            solicitudRepository.save(solicitud);


            Consola.aletaCeleste("Nueva Relación Comercial", "Relación establecida entre: ", perfilSolicitado.getUsuario().getUsername() +
                    " y " + perfilSolicitante.getUsuario().getUsername() );

            return true; // La solicitud se aceptó correctamente
        } else {
            return false; // No se encontró la solicitud con el ID proporcionado
        }
    }

    @Transactional
    public boolean cancelarSolicitud(Long solicitudId) {

        Consola.verde("CancelarSolicitud()");
        Optional<Solicitud> optionalSolicitud = solicitudRepository.findById(solicitudId);
        if (optionalSolicitud.isPresent()) {
            Solicitud solicitud = optionalSolicitud.get();
            solicitud.setAceptada(false);
            solicitud.setPendiente(false);

            Perfil perfilSolicitante = solicitud.getSolicitante();
            Perfil perfilSolicitado = solicitud.getSolicitado();

            // El solicitante tiene comercio
            Comercio comercio = perfilSolicitante.getComercio();
            // El solicitado tiene empresa
            Empresa empresa = perfilSolicitado.getEmpresa();

            // Eliminar la lista de precios asignada al comercio del solicitante por la empresa del solicitado
            if (comercio != null && empresa != null) {
                comercio.getListasAsignadas().removeIf(lista -> lista.getEmpresa().equals(empresa));
                comercioService.guardarComercio(comercio); // Guardar cambios en el comercio
            }

            //Eliminar relacion entre instituciones
            comercio.getProveedores().remove(empresa);
            empresa.getClientes().remove(comercio);

            // Eliminar la relación comercial entre los perfiles
            perfilSolicitante.getRelacioneComerciales().remove(perfilSolicitado);
            perfilSolicitado.getRelacioneComerciales().remove(perfilSolicitante);

            // Persistir Cambios
            comercioService.guardarComercio(comercio);
            empresaService.guardarEmpresa(empresa);
            perfilService.guardarPerfil(perfilSolicitante);
            perfilService.guardarPerfil(perfilSolicitado);
            solicitudRepository.save(solicitud);


            Consola.aletaRoja("Fin Relación Comercial", "Cancelación de Relación Comercial entre: ", perfilSolicitado.getUsuario().getUsername() +
                    " y " + perfilSolicitante.getUsuario().getUsername());

            solicitudRepository.delete(solicitud);
            System.out.println(Consola.textoRojo() + "Solicitud eliminada" + Consola.finTexto());

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

    public List<Solicitud> obtenerSolicitudes() {
        return solicitudRepository.findAll();
    }
}