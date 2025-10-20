package com.example.operum.advisor.strategy;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.example.operum.advisor.domain.entity.Carteira;
import com.example.operum.advisor.domain.enums.RiskProfile;

import lombok.extern.slf4j.Slf4j;

/**
 * Estratégia de seleção de carteira para perfil MODERADO.
 *
 * Foco: Equilíbrio entre crescimento e segurança. Características: - Alocação
 * balanceada (50% renda fixa, 50% renda variável) - Diversificação entre
 * diferentes classes de ativos - Busca crescimento com volatilidade controlada
 * - Retorno esperado: 14-18% ao ano
 *
 * Demonstra polimorfismo através da implementação comum da interface
 * CarteiraSelectionStrategy com comportamento específico.
 */
@Slf4j
@Component
public class ModeradaStrategy implements CarteiraSelectionStrategy {

    @Override
    public Carteira selectCarteira(RiskProfile perfilRisco, BigDecimal valorInvestimento) {
        log.info("Aplicando estratégia moderada para perfil: {}, valor: {}", perfilRisco, valorInvestimento);

        Carteira carteira = new Carteira();
        carteira.setNome("Carteira Moderada - Crescimento Equilibrado");
        carteira.setDescricao("Carteira balanceada buscando equilíbrio entre crescimento e proteção. "
                + "Alocação sugerida: 50% Renda Fixa + 50% Renda Variável. "
                + "Ideal para investidores que aceitam volatilidade moderada.");
        carteira.setPerfilRisco(RiskProfile.MODERADO);
        carteira.setRetornoEsperado(new BigDecimal("16.0")); // 16% ao ano
        carteira.setRiscoEstimado(new BigDecimal("5.5")); // Volatilidade moderada

        log.info("Carteira moderada criada com sucesso");

        return carteira;
    }

    @Override
    public String getNomeEstrategia() {
        return "Moderada";
    }

    @Override
    public String getDescricaoEstrategia() {
        return "Estratégia equilibrada entre crescimento e segurança. "
                + "Ideal para investidores que buscam retornos atrativos com risco controlado. "
                + "Alocação: 50% Renda Fixa + 50% Renda Variável.";
    }
}
