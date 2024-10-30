package com.provider.services;

import com.provider.converter.PerfilRelacionConverter;
import com.provider.dto.PerfilRelacionDTO;
import com.provider.entities.Perfil;
import com.provider.entities.Solicitud;
import com.provider.entities.Usuario;
import com.provider.repositories.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PerfilService {

    @Autowired
    private PerfilRepository perfilRepository;

    public List<PerfilRelacionDTO> obtenerRelacionesComerciales(Long perfilId) {

        Perfil perfil = perfilRepository.findById(perfilId).orElse(null);
        if (perfil != null) {

            return perfil.getRelacioneComerciales().stream()
                    .map(PerfilRelacionConverter::entityToDTO)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<PerfilRelacionDTO> obtenerPerfilesConSolicitudesPendientes(Long perfilId) {

        Perfil perfil = perfilRepository.findById(perfilId).orElse(null);
        if (perfil != null) {
            List<Solicitud> solicitudesEnviadas = perfil.getSolicitudesEnviadas();
            List<PerfilRelacionDTO> usuariosConSolicitudesPendientes = new ArrayList<>();

            for (Solicitud solicitud : solicitudesEnviadas) {
                if (solicitud.isPendiente()) {
                    Perfil perfilsolicitado = solicitud.getSolicitado();
                    PerfilRelacionDTO perfilDTO = PerfilRelacionConverter.entityToDTO(perfilsolicitado);
                    usuariosConSolicitudesPendientes.add(perfilDTO);
                }
            }
            return usuariosConSolicitudesPendientes;
        } else {
            return Collections.emptyList();
        }
    }

    public List<Perfil> obtenerProveedoresBuscados(String terminoBusqueda) {
        Usuario.TipoUsuario tipoUsuario = Usuario.TipoUsuario.PROVEEDOR;  // Filtramos solo proveedores
        return perfilRepository.buscarProveedores(tipoUsuario, terminoBusqueda);
    }


    public List<Perfil> obtenerTodosLosPerfiles() {
        return perfilRepository.findAll();
    }

    public Optional<Perfil> obtenerPerfilPorId(Long id) {
        return perfilRepository.findById(id);
    }

    public Perfil guardarPerfil(Perfil perfil) {
        return perfilRepository.save(perfil);
    }

    public void eliminarPerfilPorId(Long id) {
        perfilRepository.deleteById(id);
    }

    public Perfil obtenerPerfilPorNombre(String nombre) {
        return perfilRepository.findByNombre(nombre);
    }
}