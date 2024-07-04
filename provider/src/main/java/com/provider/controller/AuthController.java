package com.provider.controller;

import com.provider.converter.UsuarioConverter;
import com.provider.dto.UsuarioDTO;
import com.provider.entities.*;
import com.provider.other.ErrorResponse;
import com.provider.other.LoginRequest;
import com.provider.services.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired)) //Anotación para simplificar la inyeccion de dependencias.
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final RolService rolService;
    private final PerfilService perfilService;
    private final PermisoService permisoService;
    private final EmpresaService empresaService;
    private final ComercioService comercioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        Usuario usuario = usuarioService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());

        if (usuario != null) {

            if (usuario.getPerfil() == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Perfil no definido para el usuario"));
            }

            UsuarioDTO usuarioDTO = UsuarioConverter.entityToDTO(usuario);
            String redirectUrl = determineRedirectUrl(usuarioDTO);

            return ResponseEntity.ok(Map.of("user", usuarioDTO, "redirectUrl", redirectUrl));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Credenciales inválidas"));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> registrarUsuario(@RequestBody Usuario usuario) {

        if (usuarioService.existePorUsername(usuario.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "El nombre de usuario ya está en uso"));
        }

        if (usuarioService.existePorEmail(usuario.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "El correo electrónico ya está en uso"));
        }

           // Obtener permisos
           Optional<Permiso> optionalPermisoCliente = permisoService.obtenerPermisoCliente();
           Permiso permisoCliente = optionalPermisoCliente.orElse(null);
           Optional<Permiso> optionalPermisoProveedor = permisoService.obtenerPermisoProveedor();
           Permiso permisoProveedor = optionalPermisoProveedor.orElse(null);


        // Buscar roles existentes
        Rol rolCliente = rolService.findByRoleEnum(RoleEnum.CLIENT);
        Rol rolProveedor = rolService.findByRoleEnum(RoleEnum.PROVIDER);

        Usuario nuevoUsuario = Usuario.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .email(usuario.getEmail())
                .isEnabled(true)
                .credentialNoExpidered(true)
                .accountNoLocked(true)
                .accountNoExpired(true)
                .build();

        // Verificar tipo de usuario enviado desde el Cliente
        if ("PROVEEDOR".equalsIgnoreCase(usuario.getTipoUsuario().name())) {
            nuevoUsuario.setTipoUsuario(Usuario.TipoUsuario.PROVEEDOR);
            nuevoUsuario.setRoles(Set.of(rolProveedor));
        } else if ("COMERCIANTE".equalsIgnoreCase(usuario.getTipoUsuario().name())) {
            nuevoUsuario.setTipoUsuario(Usuario.TipoUsuario.COMERCIANTE);
            nuevoUsuario.setRoles(Set.of(rolCliente));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Tipo de usuario no válido"));
        }

        usuarioService.guardarUsuario(nuevoUsuario);

        Perfil nuevoPerfil = Perfil.builder()
                .nombre(usuario.getPerfil().getNombre())
                .apellido(usuario.getPerfil().getApellido())
                .fotoPerfil("default.png")
                .descripcion(null)
                .usuario(nuevoUsuario)
                .build();

        perfilService.guardarPerfil(nuevoPerfil);

        if (nuevoUsuario.getTipoUsuario() == Usuario.TipoUsuario.PROVEEDOR) {
            Empresa nuevaEmpresa = Empresa.builder()
                    .nombre(usuario.getPerfil().getEmpresa().getNombre())
                    .rubro(usuario.getPerfil().getEmpresa().getRubro())
                    .telefono(usuario.getPerfil().getEmpresa().getTelefono())
                    .domicilio(usuario.getPerfil().getEmpresa().getDomicilio())
                    .proveedor(nuevoPerfil)
                    .build();

            empresaService.guardarEmpresa(nuevaEmpresa);

        } else if (nuevoUsuario.getTipoUsuario() == Usuario.TipoUsuario.COMERCIANTE) {
            Comercio nuevoComercio = Comercio.builder()
                    .nombre(usuario.getPerfil().getComercio().getNombre())
                    .telefono(usuario.getPerfil().getComercio().getTelefono())
                    .rubro(usuario.getPerfil().getComercio().getRubro())
                    .domicilio(usuario.getPerfil().getComercio().getDomicilio())
                    .comerciante(nuevoPerfil)
                    .build();

            comercioService.guardarComercio(nuevoComercio);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Usuario registrado con éxito"));
    }

    @GetMapping("/verificarUsuario/{username}")
    public ResponseEntity<Boolean> verificarUsuarioExistente(@PathVariable String username) {
        boolean exists = usuarioService.existePorUsername(username);
        return ResponseEntity.ok(exists);
}

    @GetMapping("/verificarEmail/{email}")
    public ResponseEntity<Boolean> verificarEmailExistente(@PathVariable String email) {
        boolean exists = usuarioService.existePorEmail(email);
        return ResponseEntity.ok(exists);
    }

    private String determineRedirectUrl(UsuarioDTO usuarioDTO) {
        if (usuarioDTO.getRoles().stream().anyMatch(role -> role.getRoleEnum().equals("ADMIN"))) {
            return "/" + usuarioDTO.getUsername() + "/admin";
        } else {
            return "/" + usuarioDTO.getUsername() + "/home";
        }
    }
}

