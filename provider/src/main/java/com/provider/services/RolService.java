package com.provider.services;

import com.provider.entities.Rol;
import com.provider.entities.RolUsuario;
import com.provider.repositories.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public Rol findByRol(RolUsuario rol) {
        return rolRepository.findByRol(rol);
    }
}