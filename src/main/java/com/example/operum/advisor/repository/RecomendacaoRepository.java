package com.example.operum.advisor.repository;

import com.example.operum.advisor.domain.entity.Recomendacao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecomendacaoRepository extends JpaRepository<Recomendacao, Long> {

    List<Recomendacao> findByClienteId(Long clienteId);
}
