package com.provider.services;

import com.provider.entities.Comercio;
import com.provider.repositories.ComercioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComercioService {

    @Autowired
    private ComercioRepository comercioRepository;
    public List<Comercio> obtenerTodosLosComercio() {
        return comercioRepository.findAll();
    }
    public Optional<Comercio> obtenerComercioOpcionalPorId(Long id) {
        return comercioRepository.findById(id);
    }
    public Comercio guardarComercio(Comercio comercio) {
        return comercioRepository.save(comercio);
    }
    public void eliminarComercioPorIc(Long id) {
        comercioRepository.deleteById(id);
    }
}