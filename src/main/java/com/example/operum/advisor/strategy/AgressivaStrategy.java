package com.example.operum.advisor.strategy;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.example.operum.advisor.domain.entity.Carteira;
import com.example.operum.advisor.domain.enums.RiskProfile;

import lombok.extern.slf4j.Slf4j;

/**
 * Estratégia de seleção de carteira para perfil ARROJADO (Agressivo).
 *
 * Foco: Máximo crescimento com alta exposição a risco. Características: - Maior
 * alocação em renda variável (80-90%) - Menor exposição à renda fixa (10-20%) -
 * Busca retornos elevados aceitando alta volatilidade - Retorno esperado:
 * 20-30% ao ano
 *
 * Demonstra o princípio Open/Closed do SOLID: novas estratégias podem ser
 * adicionadas sem modificar código existente, apenas estendendo a interface
 * CarteiraSelectionStrategy.
 */
@Slf4j
@Component
public class AgressivaStrategy implements CarteiraSelectionStrategy {

    @Override
    public Carteira selectCarteira(RiskProfile perfilRisco, BigDecimal valorInvestimento) {
        log.info("Aplicando estratégia agressiva para perfil: {}, valor: {}", perfilRisco, valorInvestimento);

        Carteira carteira = new Carteira();
        carteira.setNome("Carteira Agressiva - Crescimento Acelerado");
        carteira.setDescricao("Carteira focada em máximo crescimento com alta exposição a risco. "
                + "Alocação sugerida: 15% Renda Fixa + 85% Renda Variável. "
                + "Ideal para investidores com alta tolerância a volatilidade e foco em longo prazo.");
        carteira.setPerfilRisco(RiskProfile.ARROJADO);
        carteira.setRetornoEsperado(new BigDecimal("25.0")); // 25% ao ano
        carteira.setRiscoEstimado(new BigDecimal("12.5")); // Alta volatilidade

        log.info("Carteira agressiva criada com sucesso");

        return carteira;
    }

    @Override
    public String getNomeEstrategia() {
        return "Agressiva";
    }

    @Override
    public String getDescricaoEstrategia() {
        return "Estratégia focada em crescimento acelerado com alta exposição a risco. "
                + "Ideal para investidores experientes com horizonte de longo prazo. "
                + "Alocação: 15% Renda Fixa + 85% Renda Variável.";
    }
}
