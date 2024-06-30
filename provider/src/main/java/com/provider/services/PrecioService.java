package com.provider.services;

import com.provider.entities.Precio;
import com.provider.repositories.PrecioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrecioService {

    @Autowired
    private PrecioRepository precioRepository;

    public List<Precio> obtenerPreciosPorProducto(Long productoId) {
        return precioRepository.findByProductoId(productoId);
    }
    public void guardarPrecio(Precio precio) {
        precioRepository.save(precio);
    }

    public void eliminarPrecioPorId(Long id){
        precioRepository.deleteById(id);
    }


}
