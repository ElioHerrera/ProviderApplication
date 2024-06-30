package com.provider.services;

import com.provider.converter.ProductoConverter;
import com.provider.dto.ProductoDTO;
import com.provider.entities.Producto;
import com.provider.repositories.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private final ProductoRepository productoRepository;

    @Autowired
    private ProductoConverter productoConverter;




    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerProductoOpcionalPorId(Long id) {
        return productoRepository.findById(id);
    }

    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }


    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }

    public ProductoDTO actualizarProducto(Long id, ProductoDTO productoDTO) {
        Optional<Producto> productoOptional = productoRepository.findById(id);
        if (productoOptional.isEmpty()) {
            throw new EntityNotFoundException("Producto no encontrado");
        }

        Producto producto = productoOptional.get();

        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setCodigo(productoDTO.getCodigo());
        producto = productoRepository.save(producto);



        return productoConverter.entityToDTO(producto);
    }


}