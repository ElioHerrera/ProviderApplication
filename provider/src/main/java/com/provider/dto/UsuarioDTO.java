package com.provider.dto;

import com.provider.entities.Rol;
import com.provider.entities.Usuario;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String email;
    private String username;
    private Usuario.TipoUsuario tipoUsuario;
    private PerfilDTO perfil;
    private boolean isEnabled;
    private Set<RolDTO> roles;
}