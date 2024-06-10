package com.provider.repositories;

import com.provider.entities.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    Solicitud findBySolicitanteIdAndSolicitadoId(Long solicitanteId, Long solicitadoId);
}
