package com.provider.repositories;

import com.provider.entities.Rol;
import com.provider.entities.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    Rol findByRoleEnum(RoleEnum roleEnum);

}
