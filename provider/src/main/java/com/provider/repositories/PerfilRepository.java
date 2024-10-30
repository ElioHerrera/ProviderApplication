package com.provider.repositories;

import com.provider.entities.Perfil;
import com.provider.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    Perfil findByNombre(String nombre);

//    List<Perfil> findByUsuarioTipoUsuarioAndNombreContainingIgnoreCase(Usuario.TipoUsuario tipoUsuario, String nombre);

    // Búsqueda por nombre, apellido, email o nombre de empresa (si aplica)
//    List<Perfil> findByUsuarioTipoUsuarioAndNombreContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrUsuarioEmailContainingIgnoreCaseOrEmpresaNombreContainingIgnoreCase(
//            Usuario.TipoUsuario tipoUsuario, String nombre, String apellido, String email, String empresa);

    // Búsqueda por nombre, apellido, email o nombre de empresa solo para usuarios con tipo PROVEEDOR
//    List<Perfil> findByUsuarioTipoUsuarioAndNombreContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrUsuarioEmailContainingIgnoreCaseOrEmpresaNombreContainingIgnoreCase(
//            String nombre, String apellido, String email, String empresa
//    );

    // Búsqueda solo para perfiles asociados a usuarios con tipo "PROVEEDOR"
//    List<Perfil> findByUsuarioTipoUsuarioAndNombreContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrUsuarioEmailContainingIgnoreCaseOrEmpresaNombreContainingIgnoreCase(
//            Usuario.TipoUsuario tipoUsuario,
//            String nombre,
//            String apellido,
//            String email,
//            String empresa
//    );



    @Query("SELECT p FROM Perfil p WHERE p.usuario.tipoUsuario = :tipoUsuario AND " +
            "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :terminoBusqueda, '%')) OR " +
            "LOWER(p.apellido) LIKE LOWER(CONCAT('%', :terminoBusqueda, '%')) OR " +
            "LOWER(p.usuario.email) LIKE LOWER(CONCAT('%', :terminoBusqueda, '%')) OR " +
            "LOWER(p.empresa.nombre) LIKE LOWER(CONCAT('%', :terminoBusqueda, '%')))")
    List<Perfil> buscarProveedores(@Param("tipoUsuario") Usuario.TipoUsuario tipoUsuario,
                                   @Param("terminoBusqueda") String terminoBusqueda);



}


