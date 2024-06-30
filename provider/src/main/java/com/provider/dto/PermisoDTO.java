package com.provider.dto;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermisoDTO {
    private Long idPermiso;
    private String nombrePermiso;
}