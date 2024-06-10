package com.provider.services;

import com.provider.entities.Permiso;
import com.provider.repositories.PermisoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class PermisoService {

    @Autowired
    private PermisoRepository permisoRepository;

    public Optional<Permiso> obtenerPermisoCliente() {
        return permisoRepository.findByNombrePermiso("Cliente");
    }
    public Optional<Permiso> obtenerPermisoProveedor() {
        return permisoRepository.findByNombrePermiso("Proveedor");
    }


}