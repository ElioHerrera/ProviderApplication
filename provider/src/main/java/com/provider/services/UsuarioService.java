package com.provider.services;

import com.provider.converter.UsuarioConverter;
import com.provider.dto.UsuarioDTO;
import com.provider.entities.Usuario;
import com.provider.other.Consola;
import com.provider.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario autenticacion(String username, String password) {

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

    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO usuarioDTO) {

        Usuario usuario = obtenerUsuarioPorId(id);
        UsuarioDTO usuarioActual = UsuarioConverter.entityToDTO(usuario);
        UsuarioDTO usuarioActualizado = usuarioDTO;

        System.out.println("Entro al metodo");

        if (!usuarioActual.getPerfil().getNombre().equals(usuarioActualizado.getPerfil().getNombre())) {
            usuario.getPerfil().setNombre(usuarioActualizado.getPerfil().getNombre());
            Consola.amarillo("Nuevo nombre: " + usuario.getPerfil().getNombre());
        }

        if (!usuarioActual.getPerfil().getApellido().equals(usuarioActualizado.getPerfil().getApellido())) {
            usuario.getPerfil().setApellido(usuarioActualizado.getPerfil().getNombre());
            Consola.amarillo("Nuevo Apellido: " + usuario.getPerfil().getApellido());
        }
        if(!usuarioActual.getPerfil().getDescripcion().equals(usuarioActualizado.getPerfil().getDescripcion())){
            usuario.getPerfil().setDescripcion(usuarioActualizado.getPerfil().getDescripcion());
            Consola.amarillo("Nueva Descripcion: " + usuario.getPerfil().getDescripcion());
        }
        if(esComerciante(id)){

            if (!usuarioActual.getPerfil().getComercio().getNombre().equals(usuarioActualizado.getPerfil().getComercio().getNombre())) {
                usuario.getPerfil().getComercio().setNombre(usuarioActualizado.getPerfil().getComercio().getNombre());
                System.out.println("Nuevo nombre del comercio: " + usuario.getPerfil().getComercio().getNombre());
            }
            if(!usuarioActual.getPerfil().getComercio().getRubro().equals(usuarioActualizado.getPerfil().getComercio().getRubro())){
                usuario.getPerfil().getComercio().setRubro(usuarioActualizado.getPerfil().getComercio().getRubro());
                System.out.println("Nuevo rubro del comercio: " + usuario.getPerfil().getComercio().getRubro());
            }
            if (!usuarioActual.getPerfil().getComercio().getDomicilio().equals(usuarioActualizado.getPerfil().getComercio().getDomicilio())) {
                usuario.getPerfil().getComercio().setDomicilio(usuarioActualizado.getPerfil().getComercio().getDomicilio());
                System.out.println("Nuevo domicilio del comercio: " + usuario.getPerfil().getComercio().getDomicilio());
            }
            if (!usuarioActual.getPerfil().getComercio().getTelefono().equals(usuarioActualizado.getPerfil().getComercio().getTelefono())) {
                usuario.getPerfil().getComercio().setTelefono(usuarioActualizado.getPerfil().getComercio().getTelefono());
                System.out.println("Nuevo teléfono del comercio: " + usuario.getPerfil().getComercio().getTelefono());
            }

        }
        if (esProveedor(id)) {

            if (!usuarioActual.getPerfil().getEmpresa().getNombre().equals(usuarioActualizado.getPerfil().getEmpresa().getNombre())) {
                usuario.getPerfil().getEmpresa().setNombre(usuarioActualizado.getPerfil().getEmpresa().getNombre());
                System.out.println("Nuevo nombre de la empresa: " + usuario.getPerfil().getEmpresa().getNombre());
            }

            if (!usuarioActual.getPerfil().getEmpresa().getRubro().equals(usuarioActualizado.getPerfil().getEmpresa().getRubro())) {
                usuario.getPerfil().getEmpresa().setRubro(usuarioActualizado.getPerfil().getEmpresa().getRubro());
                System.out.println("Nuevo rubro de la empresa: " + usuario.getPerfil().getEmpresa().getRubro());
            }

            if (!usuarioActual.getPerfil().getEmpresa().getDomicilio().equals(usuarioActualizado.getPerfil().getEmpresa().getDomicilio())) {
                usuario.getPerfil().getEmpresa().setDomicilio(usuarioActualizado.getPerfil().getEmpresa().getDomicilio());
                System.out.println("Nuevo domicilio de la empresa: " + usuario.getPerfil().getEmpresa().getDomicilio());
            }

            if (!usuarioActual.getPerfil().getEmpresa().getTelefono().equals(usuarioActualizado.getPerfil().getEmpresa().getTelefono())) {
                usuario.getPerfil().getEmpresa().setTelefono(usuarioActualizado.getPerfil().getEmpresa().getTelefono());
                System.out.println("Nuevo teléfono de la empresa: " + usuario.getPerfil().getEmpresa().getTelefono());
            }

        }





        guardarUsuario(usuario);
        System.out.println("usuarioguardado");

        return usuarioActualizado;
    }




    public boolean esProveedor(Long usuarioId) {
        Usuario usuario = obtenerUsuarioPorId(usuarioId);
        return usuario != null && usuario.getTipoUsuario() == Usuario.TipoUsuario.PROVEEDOR;
    }

    public boolean esComerciante(Long usuarioId) {
        Usuario usuario = obtenerUsuarioPorId(usuarioId);
        return usuario != null && usuario.getTipoUsuario() == Usuario.TipoUsuario.COMERCIANTE;
    }


    public boolean esAdministrador(Long usuarioId) {
        Usuario usuario = obtenerUsuarioPorId(usuarioId);
        return usuario != null && usuario.getTipoUsuario() == Usuario.TipoUsuario.ADMINISTRADOR;
    }


}