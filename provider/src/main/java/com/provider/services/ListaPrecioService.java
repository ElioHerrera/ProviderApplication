package com.provider.services;

import com.provider.entities.ListaPrecio;
import com.provider.repositories.ListaPrecioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ListaPrecioService {

    @Autowired
    private ListaPrecioRepository listaPrecioRepository;



    public List<ListaPrecio> findAll() {
        return listaPrecioRepository.findAll();
    }

    public Optional<ListaPrecio> findById(Long id) {
        return listaPrecioRepository.findById(id);
    }

    public ListaPrecio guardarListaPrecio(ListaPrecio listaPrecio) {
        return listaPrecioRepository.save(listaPrecio);
    }

    public void deleteById(Long id) {
        listaPrecioRepository.deleteById(id);
    }



}