package com.provider;

import com.provider.entities.Empresa;
import com.provider.repositories.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MigracionEmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    //EJEMPLO DE MISGRACION DE DATOS
//    public void migrarListasDeProductosYListasDePrecios() {
//        List<Empresa> empresas = empresaRepository.findAll();
//
//        for (Empresa empresa : empresas) {
//            if (empresa.getProductos() == null) {
//                empresa.setProductos(new ArrayList<>());
//            }
//            if (empresa.getListasPrecios() == null) {
//                empresa.setListasPrecios(new ArrayList<>());
//            }
//            // Guardar la empresa actualizada
//            empresaRepository.save(empresa);
//        }
//    }
}