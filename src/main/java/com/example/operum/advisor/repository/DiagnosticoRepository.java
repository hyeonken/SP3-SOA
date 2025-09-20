package com.example.operum.advisor.repository;

import com.example.operum.advisor.domain.entity.Diagnostico;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long> {

    Optional<Diagnostico> findByClienteId(Long clienteId);
}
