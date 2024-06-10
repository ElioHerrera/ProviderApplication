package com.provider.converter;

import com.provider.dto.*;
import com.provider.entities.Perfil;
import com.provider.entities.Producto;
import java.util.ArrayList;
import java.util.List;


public class PerfilRelacionConverter {

    public static PerfilRelacionDTO entityToDTO(Perfil perfil) {
        PerfilRelacionDTO perfilRelacionDTO = new PerfilRelacionDTO();
        perfilRelacionDTO.setId(perfil.getId());
        perfilRelacionDTO.setNombre(perfil.getNombre());
        perfilRelacionDTO.setApellido(perfil.getApellido());
        perfilRelacionDTO.setFotoPerfil(perfil.getFotoPerfil());

        if (perfil.getUsuario() != null){
            System.out.println(perfil.getUsuario().getPerfil().getNombre());
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setId(perfil.getUsuario().getId());
            usuarioDTO.setEmail(perfil.getUsuario().getEmail());
            perfilRelacionDTO.setUsuario(usuarioDTO);
        }

        if (perfil.getComercio() != null) {
            ComercioDTO comercioDTO = new ComercioDTO();
            comercioDTO.setId(perfil.getComercio().getId());
            comercioDTO.setNombre(perfil.getComercio().getNombre());
            comercioDTO.setTelefono(perfil.getComercio().getTelefono());
            comercioDTO.setRubro(perfil.getComercio().getRubro());
            comercioDTO.setDomicilio(perfil.getComercio().getDomicilio());
            perfilRelacionDTO.setComercio(comercioDTO);
        }

        if (perfil.getEmpresa() != null) {
            EmpresaDTO empresaDTO = new EmpresaDTO();
            empresaDTO.setId(perfil.getEmpresa().getId());
            empresaDTO.setNombre(perfil.getEmpresa().getNombre());
            empresaDTO.setRubro(perfil.getEmpresa().getRubro());
            empresaDTO.setTelefono(perfil.getEmpresa().getTelefono());
            empresaDTO.setDomicilio(perfil.getEmpresa().getDomicilio());
            perfilRelacionDTO.setEmpresa(empresaDTO);
        }

        if (perfil.getEmpresa() != null && perfil.getEmpresa().getProductos() != null) {
            List<ProductoDTO> productosDTO = new ArrayList<>();
            for (Producto producto : perfil.getEmpresa().getProductos()) {
                ProductoDTO productoDTO = new ProductoDTO();
                productoDTO.setId(producto.getId());
                productoDTO.setNombre(producto.getNombre());
                productoDTO.setDescripcion(producto.getDescripcion());
                productoDTO.setPrecio(producto.getPrecio());
                productosDTO.add(productoDTO);
            }
            perfilRelacionDTO.setProductos(productosDTO);
        }

        return perfilRelacionDTO;
    }
}