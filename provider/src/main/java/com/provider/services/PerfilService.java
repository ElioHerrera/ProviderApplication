package com.provider.services;

import com.provider.converter.PerfilRelacionConverter;
import com.provider.dto.PerfilRelacionDTO;
import com.provider.entities.Perfil;
import com.provider.entities.Solicitud;
import com.provider.repositories.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PerfilService {

    @Autowired
    private PerfilRepository perfilRepository;

    public List<Solicitud> obtenerSolicitudesRecibidas(Long perfilId) {
        Perfil perfil = perfilRepository.findById(perfilId).orElse(null);
        if (perfil != null) {
            return perfil.getSolicitudesRecibidas();
        } else {
            // Manejar el caso cuando no se encuentra el perfil con el ID especificado
            return null;
        }
    }

    public List<PerfilRelacionDTO> obtenerRelacionesComerciales(Long perfilId) {
        Perfil perfil = perfilRepository.findById(perfilId).orElse(null);
        if (perfil != null) {
            return perfil.getRelacioneComerciales().stream()
                    .map(PerfilRelacionConverter::entityToDTO)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<Perfil> findAll() {
        return perfilRepository.findAll();
    }

    public Optional<Perfil> findById(Long id) {
        return perfilRepository.findById(id);
    }

    public Perfil save(Perfil perfil) {
        return perfilRepository.save(perfil);
    }

    public void deleteById(Long id) {
        perfilRepository.deleteById(id);
    }

    public Perfil findByNombre(String nombre) {
        return perfilRepository.findByNombre(nombre);
    }
}