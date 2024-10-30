package com.provider.controller;

import com.provider.entities.Comercio;
import com.provider.entities.Empresa;
import com.provider.entities.ListaPrecio;
import com.provider.entities.Usuario;
import com.provider.other.Consola;
import com.provider.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/precios")
public class PreciosController {

    @Autowired
    private ListaPrecioService listaPrecioService;

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ComercioService comercioService;




    @PostMapping("/{userId}/asignarLista/{idCliente}/{idLista}")
    public ResponseEntity<?> asignarListaPrecios(@PathVariable("userId") Long userId, @PathVariable("idCliente") Long idCliente, @PathVariable("idLista") Long idLista) {

        try {

            Usuario usuarioComerciante = usuarioService.obtenerUsuarioPorId(idCliente);
            Comercio comercio = usuarioComerciante.getPerfil().getComercio();

            // Obtener el proveedor y su empresa
            Usuario usuarioProveedor = usuarioService.obtenerUsuarioPorId(userId);
            Empresa empresa = usuarioProveedor.getPerfil().getEmpresa();

            // Obtener la lista de precios actualmente asignada al comercio
            List<ListaPrecio> listasAsignadas = comercio.getListasAsignadas();
            ListaPrecio listaActualAsignada = null;
            for (ListaPrecio lista : listasAsignadas) {
                if (lista.getEmpresa().equals(empresa)) {
                    listaActualAsignada = lista;
                    break;
                }
            }

            // Si hay una lista actualmente asignada, eliminarla
            if (listaActualAsignada != null) {
                listasAsignadas.remove(listaActualAsignada);
            }

            // Obtener la nueva lista de precios a asignar
            Optional<ListaPrecio> listaOptional = listaPrecioService.findById(idLista);
            ListaPrecio listaNueva = listaOptional.orElseThrow(() -> new RuntimeException("Lista de precios no encontrada"));

            // Asignar la nueva lista de precios al comercio
            listasAsignadas.add(listaNueva);
            comercio.setListasAsignadas(listasAsignadas);
            comercioService.guardarComercio(comercio);


            Consola.rosa("lista asignada");

            return ResponseEntity.ok(Collections.singletonMap("message", "Lista de precios asignada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Error al asignar la lista de precios"));
        }
    }

}
