package com.provider.controller;

import com.provider.converter.UsuarioConverter;
import com.provider.dto.UsuarioDTO;
import com.provider.login.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import com.provider.entities.*;
import com.provider.login.LoginRequest;
import com.provider.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolService rolService;

    @Autowired
    private PerfilService perfilService;

    @Autowired
    private PermisoService permisoService;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private ComercioService comercioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Usuario usuario = usuarioService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        if (usuario != null) {
            // Asegúrate de que el perfil esté cargado
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

        if (usuarioService.existsByUsername(usuario.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "El nombre de usuario ya está en uso"));
        }

        if (usuarioService.existsByEmail(usuario.getEmail())) {
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

        // Verificar tipo de usuario elegido en el front
        if ("PROVEEDOR".equalsIgnoreCase(usuario.getTipoUsuario().name())) {
            nuevoUsuario.setTipoUsuario(Usuario.TipoUsuario.PROVEEDOR);
            nuevoUsuario.setRoles(Set.of(rolProveedor));
        } else if ("COMERCIANTE".equalsIgnoreCase(usuario.getTipoUsuario().name())) {
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
        boolean exists = usuarioService.existsByUsername(username);
        return ResponseEntity.ok(exists);
}

    @GetMapping("/verificarEmail/{email}")
    public ResponseEntity<Boolean> verificarEmailExistente(@PathVariable String email) {
        boolean exists = usuarioService.existsByEmail(email);
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

