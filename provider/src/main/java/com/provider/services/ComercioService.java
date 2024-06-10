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

    public List<Comercio> findAll() {
        return comercioRepository.findAll();
    }

    public Optional<Comercio> findById(Long id) {
        return comercioRepository.findById(id);
    }

    public Comercio save(Comercio comercio) {
        return comercioRepository.save(comercio);
    }

    public void deleteById(Long id) {
        comercioRepository.deleteById(id);
    }
}