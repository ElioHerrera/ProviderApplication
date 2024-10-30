package com.provider.repositories;

import com.provider.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("SELECT COALESCE(MAX(p.numeroPedido), 0) FROM Pedido p WHERE p.cliente.id = :comercioId AND p.proveedor.id = :empresaId")
    int findMaxNumeroPedidoByClienteAndProveedor(@Param("comercioId") Long comercioId, @Param("empresaId") Long empresaId);

}