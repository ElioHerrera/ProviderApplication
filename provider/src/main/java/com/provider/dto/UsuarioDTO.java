package com.provider.dto;

import com.provider.entities.Usuario;
import lombok.*;

import java.util.Set;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String username;
    private String email;
    private Usuario.TipoUsuario tipoUsuario;
    private PerfilDTO perfil;
    private boolean isEnabled;
    private Set<RolDTO> roles;
}