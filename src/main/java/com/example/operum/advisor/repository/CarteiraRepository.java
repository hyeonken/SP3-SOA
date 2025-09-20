package com.example.operum.advisor.repository;

import com.example.operum.advisor.domain.entity.Carteira;
import com.example.operum.advisor.domain.enums.RiskProfile;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarteiraRepository extends JpaRepository<Carteira, Long> {

    @EntityGraph(attributePaths = {"itens", "itens.ativo"})
    List<Carteira> findByPerfilRisco(RiskProfile perfilRisco);
}
