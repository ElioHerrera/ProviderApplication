package com.provider.repositories;

import com.provider.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    public Usuario findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<Usuario> findByTipoUsuario(Usuario.TipoUsuario tipoUsuario);

    //public Usuario findByEmail(String email);

}
