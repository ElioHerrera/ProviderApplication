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

    public boolean existePorUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    public boolean existePorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario obtenerUsuarioPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public List<Usuario> obtenerComerciantes() {
        return usuarioRepository.findByTipoUsuario(Usuario.TipoUsuario.COMERCIANTE);
    }
    public List<Usuario> obtenerProveedores() {return usuarioRepository.findByTipoUsuario(Usuario.TipoUsuario.PROVEEDOR); }

    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }
    public Optional<Usuario> obtenerUsuarioOptionalPorId(Long id) {return usuarioRepository.findById(id);}
    public void eliminarUsuarioPorId(Long id) {usuarioRepository.deleteById(id);}

}