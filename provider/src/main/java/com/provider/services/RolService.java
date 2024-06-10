package com.provider.services;

import com.provider.entities.Rol;
import com.provider.entities.RoleEnum;
import com.provider.entities.Usuario;
import com.provider.repositories.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public Rol findByRoleEnum(RoleEnum roleEnum) {
        return rolRepository.findByRoleEnum(roleEnum);
    }
}