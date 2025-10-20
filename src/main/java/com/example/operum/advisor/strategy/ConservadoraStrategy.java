package com.example.operum.advisor.strategy;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.example.operum.advisor.domain.entity.Carteira;
import com.example.operum.advisor.domain.enums.RiskProfile;

import lombok.extern.slf4j.Slf4j;

/**
 * Estratégia de seleção de carteira para perfil CONSERVADOR.
 *
 * Foco: Segurança e preservação de capital. Características: - Maior alocação
 * em renda fixa (70-80%) - Menor exposição à renda variável (20-30%) - Prioriza
 * liquidez e estabilidade - Retorno esperado: 8-12% ao ano
 *
 * Demonstra o padrão Strategy permitindo troca dinâmica de algoritmo de seleção
 * de carteira em tempo de execução.
 */
@Slf4j
@Component
public class ConservadoraStrategy implements CarteiraSelectionStrategy {

    @Override
    public Carteira selectCarteira(RiskProfile perfilRisco, BigDecimal valorInvestimento) {
        log.info("Aplicando estratégia conservadora para perfil: {}, valor: {}", perfilRisco, valorInvestimento);

        Carteira carteira = new Carteira();
        carteira.setNome("Carteira Conservadora - Preservação de Capital");
        carteira.setDescricao("Carteira focada em segurança e estabilidade. "
                + "Alocação sugerida: 75% Renda Fixa + 25% Renda Variável. "
                + "Ideal para preservação de capital com baixa volatilidade.");
        carteira.setPerfilRisco(RiskProfile.CONSERVADOR);
        carteira.setRetornoEsperado(new BigDecimal("10.5")); // 10.5% ao ano
        carteira.setRiscoEstimado(new BigDecimal("2.5")); // Baixa volatilidade

        log.info("Carteira conservadora criada com sucesso");

        return carteira;
    }

    @Override
    public String getNomeEstrategia() {
        return "Conservadora";
    }

    @Override
    public String getDescricaoEstrategia() {
        return "Estratégia focada em segurança e preservação de capital. "
                + "Ideal para investidores avessos a risco que priorizam estabilidade. "
                + "Alocação: 75% Renda Fixa + 25% Renda Variável.";
    }
}
