package com.example.operum.advisor.strategy;

import java.math.BigDecimal;

import com.example.operum.advisor.domain.entity.Carteira;
import com.example.operum.advisor.domain.enums.RiskProfile;

/**
 * Strategy Pattern para seleção de carteiras de investimento. Demonstra
 * polimorfismo e despacho dinâmico de métodos baseado no perfil de risco.
 *
 * Cada implementação desta interface define uma estratégia diferente para
 * selecionar carteiras adequadas ao perfil do investidor.
 *
 * @author Operum Advisor Team
 * @see ConservadoraStrategy
 * @see ModeradaStrategy
 * @see AgressivaStrategy
 */
public interface CarteiraSelectionStrategy {

    /**
     * Seleciona a carteira de investimento mais adequada baseada no perfil de
     * risco e valor disponível para investimento.
     *
     * Demonstra o princípio Open/Closed (SOLID): novas estratégias podem ser
     * adicionadas sem modificar o código existente.
     *
     * @param perfilRisco Perfil de risco do investidor (CONSERVADOR, MODERADO,
     * AGRESSIVO)
     * @param valorInvestimento Valor disponível para investimento
     * @return Carteira recomendada para o perfil
     */
    Carteira selectCarteira(RiskProfile perfilRisco, BigDecimal valorInvestimento);

    /**
     * Retorna o nome descritivo da estratégia.
     *
     * @return Nome da estratégia (ex: "Conservadora", "Moderada", "Agressiva")
     */
    String getNomeEstrategia();

    /**
     * Retorna a descrição detalhada da estratégia de investimento.
     *
     * @return Descrição explicando o foco da estratégia
     */
    String getDescricaoEstrategia();
}
