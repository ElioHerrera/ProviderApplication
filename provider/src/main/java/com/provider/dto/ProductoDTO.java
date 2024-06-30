package com.provider.dto;

import com.provider.entities.Empresa;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDTO {

        private Long idProducto;
        private Long idUsuario;
        private int codigo;
        private String nombre;
        private String descripcion;
        private String fotoProducto;
        private boolean isEnabled;
        private double lista1;
        private double lista2;
        private double lista3;

}
