package com.provider.controller;

import com.provider.converter.UsuarioConverter;
import com.provider.dto.UsuarioDTO;
import com.provider.entities.*;
import com.provider.other.ErrorResponse;
import com.provider.other.JwtUsuario;
import com.provider.other.LoginRequest;
import com.provider.other.Consola;
import com.provider.services.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Map;
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
    private JwtUsuario jwtUsuario;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Usuario usuario = usuarioService.autenticacion(loginRequest.getUsername(), loginRequest.getPassword());
        if (usuario != null) {
            if (usuario.getPerfil() == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Perfil no definido para el usuario"));
            }
            // Token JWT
            String token = jwtUsuario.crearToken(usuario.getUsername(), usuario.getRoles());
            UsuarioDTO usuarioDTO = UsuarioConverter.entityToDTO(usuario);
            String redireccionUrl = determinarUrl(usuarioDTO);


            System.out.println(Consola.textoAzul() + "Ingreso del usuario: " + usuarioDTO.getUsername() + Consola.finTexto());
            return ResponseEntity.ok(Map.of("usuario", usuarioDTO, "token", token, "redireccionUrl", redireccionUrl));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Credenciales inválidas"));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> registrarUsuario(@RequestBody Usuario usuarioFormulario) {

        if (usuarioService.existePorUsername(usuarioFormulario.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "El nombre de usuario ya está en uso"));
        }

        if (usuarioService.existePorEmail(usuarioFormulario.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "El correo electrónico ya está en uso"));
        }

        // Buscar roles y permisos

        Rol rolCliente = rolService.findByRol(RolUsuario.CLIENT);
        HashSet<Permiso> listaPermisoCliente = new HashSet<>();
        listaPermisoCliente.add(permisoService.obtenerPermisoCliente());
        rolCliente.setListaDePermisos(listaPermisoCliente);

        Rol rolProveedor = rolService.findByRol(RolUsuario.PROVIDER);
        HashSet<Permiso> listaPermisoProveedor = new HashSet<>();
        listaPermisoProveedor.add(permisoService.obtenerPermisoProveedor());
        rolProveedor.setListaDePermisos(listaPermisoProveedor);

        Usuario nuevoUsuario = Usuario.builder()
                .username(usuarioFormulario.getUsername())
                .password(usuarioFormulario.getPassword())
                .email(usuarioFormulario.getEmail())
                .isEnabled(true)
                .credentialNoExpidered(true)
                .accountNoLocked(true)
                .accountNoExpired(true)
                .build();

        // Verificar tipo de usuario enviado desde el Cliente
        if ("PROVEEDOR".equalsIgnoreCase(usuarioFormulario.getTipoUsuario().name())) {
            nuevoUsuario.setTipoUsuario(Usuario.TipoUsuario.PROVEEDOR);
            nuevoUsuario.setRoles(Set.of(rolProveedor));
        } else if ("COMERCIANTE".equalsIgnoreCase(usuarioFormulario.getTipoUsuario().name())) {
            nuevoUsuario.setTipoUsuario(Usuario.TipoUsuario.COMERCIANTE);
            nuevoUsuario.setRoles(Set.of(rolCliente));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Tipo de usuario no válido"));
        }

        usuarioService.guardarUsuario(nuevoUsuario);

        Perfil nuevoPerfil = Perfil.builder()
                .nombre(usuarioFormulario.getPerfil().getNombre())
                .apellido(usuarioFormulario.getPerfil().getApellido())
                .fotoPerfil("default.png")
                .descripcion(null)
                .usuario(nuevoUsuario)
                .build();

        perfilService.guardarPerfil(nuevoPerfil);

        if (nuevoUsuario.getTipoUsuario() == Usuario.TipoUsuario.PROVEEDOR) {
            Empresa nuevaEmpresa = Empresa.builder()
                    .nombre(usuarioFormulario.getPerfil().getEmpresa().getNombre())
                    .rubro(usuarioFormulario.getPerfil().getEmpresa().getRubro())
                    .telefono(usuarioFormulario.getPerfil().getEmpresa().getTelefono())
                    .domicilio(usuarioFormulario.getPerfil().getEmpresa().getDomicilio())
                    .proveedor(nuevoPerfil)
                    .build();

            empresaService.guardarEmpresa(nuevaEmpresa);

        } else if (nuevoUsuario.getTipoUsuario() == Usuario.TipoUsuario.COMERCIANTE) {
            Comercio nuevoComercio = Comercio.builder()
                    .nombre(usuarioFormulario.getPerfil().getComercio().getNombre())
                    .telefono(usuarioFormulario.getPerfil().getComercio().getTelefono())
                    .rubro(usuarioFormulario.getPerfil().getComercio().getRubro())
                    .domicilio(usuarioFormulario.getPerfil().getComercio().getDomicilio())
                    .comerciante(nuevoPerfil)
                    .build();

            comercioService.guardarComercio(nuevoComercio);

            Consola.aletaVerde("Nuevo Usuario Registrado", nuevoUsuario.getEmail(), nuevoUsuario.getUsername() + " tipo ususario : " + nuevoUsuario.getTipoUsuario().name());
            //System.out.println(Mensaje.textoAmarillo()+"Nuevo usuario: "+ nuevoUsuario.getUsername()+ Mensaje.finTexto());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Usuario registrado con éxito"));
    }

    @GetMapping("/verifyexistinguser/{username}")
    public ResponseEntity<Boolean> verificarExisteUsername(@PathVariable String username) {
        boolean existe = usuarioService.existePorUsername(username);
        return ResponseEntity.ok(existe);
}

    @GetMapping("/verifyexistingemail/{email}")
    public ResponseEntity<Boolean> VerificarExisteEmail(@PathVariable String email) {
        boolean existe = usuarioService.existePorEmail(email);
        return ResponseEntity.ok(existe);
    }

    private String determinarUrl(UsuarioDTO usuarioDTO) {
        if (usuarioDTO.getRoles().stream().anyMatch(roles -> roles.getRol().equals("ADMIN"))) {
            return "/" + usuarioDTO.getUsername() + "/admin";
        } else {
            return "/" + usuarioDTO.getUsername() + "/home";
        }
    }
}
