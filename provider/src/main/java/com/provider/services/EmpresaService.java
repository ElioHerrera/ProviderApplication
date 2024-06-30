package com.provider.services;

import com.provider.entities.Empresa;
import com.provider.repositories.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    public List<Empresa> obtenerTodasLasEmpresas() {
        return empresaRepository.findAll();
    }

    public Optional<Empresa> obtenerEmpresaOpcionalPorId(Long id) {
        return empresaRepository.findById(id);
    }

    public Empresa guardarEmpresa(Empresa empresa) {
        return empresaRepository.save(empresa);
    }

    public void guardarEmpresaPorId(Long id) {
        empresaRepository.deleteById(id);
    }

}