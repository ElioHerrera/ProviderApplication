package com.provider.services;

import com.provider.entities.Usuario;
import com.provider.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    public Usuario authenticate(String username, String password) {

        Usuario usuario = usuarioRepository.findByUsername(username);
        if (usuario != null && usuario.getPassword().equals(password)) {
            return usuario;
        }
        return null;
    }
    public List<Usuario> obtenerProveedores() {return usuarioRepository.findByTipoUsuario(Usuario.TipoUsuario.PROVEEDOR); }
    public Usuario saveUser(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
    public boolean existsByUsername(String username) { return usuarioRepository.existsByUsername(username);}
    public boolean existsByEmail(String email) { return usuarioRepository.existsByEmail(email);    }
    public Usuario findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }
    public Optional<Usuario> obtenerUsuarioOptionalPorId(Long id) {return usuarioRepository.findById(id);}
    public void eliminarUsuarioPorId(Long id) {usuarioRepository.deleteById(id);}

    //public Usuario findByEmail(String email) {return usuarioRepository.findByEmail(email);}
    //public List<Usuario> findAll() { return usuarioRepository.findAll();}
    //public Optional<Usuario> findById(Long id) {return usuarioRepository.findById(id);}
    //public Optional<Perfil> findPerfilByUsername(String username) {Usuario usuario = findByUsername(username); return Optional.ofNullable(usuario != null ? usuario.getPerfil() : null);}

}