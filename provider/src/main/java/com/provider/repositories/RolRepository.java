package com.provider.repositories;

import com.provider.entities.Rol;
import com.provider.entities.RolUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    Rol findByRol(RolUsuario rol);

}
