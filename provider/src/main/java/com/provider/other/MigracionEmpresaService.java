package com.provider.other;

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

    //MIGRACIÓN DE DATOS
    /*

    * Se utilizó porque inicialmente se crearon empresas sin inicializar sus listas de precios,
    ahora en la clase : ProductoController en el método crearProducto() cuando un usuario crea
    el primer producto se crea los precios, lista de productos de la empresa y listas de precios
    relacianadas con el Producto y la Empresa.

    * @Transactional >>> es utilizado para evitar persistir algun cambio en la base de datos sin
    que se confirme la finalización del proceso

    */
    public void migrarListasDeProductosYListasDePrecios() {
        List<Empresa> empresas = empresaRepository.findAll();

        for (Empresa empresa : empresas) {
            if (empresa.getProductos() == null) {
                empresa.setProductos(new ArrayList<>());
            }
            if (empresa.getListasPrecios() == null) {
                empresa.setListasPrecios(new ArrayList<>());
            }
            empresaRepository.save(empresa);
        }
    }
}