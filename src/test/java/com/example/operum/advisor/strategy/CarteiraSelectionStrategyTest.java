package com.example.operum.advisor.strategy;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.operum.advisor.domain.entity.Carteira;
import com.example.operum.advisor.domain.enums.RiskProfile;

/**
 * Testes para o padrão Strategy de seleção de carteira.
 *
 * Demonstra POLIMORFISMO e DESPACHO DINÂMICO através do teste de diferentes
 * implementações da mesma interface CarteiraSelectionStrategy.
 *
 * Este teste valida os princípios SOLID: - Open/Closed: Novas estratégias podem
 * ser adicionadas sem modificar código existente - Liskov Substitution: Todas
 * as estratégias são substituíveis pela interface comum - Dependency Inversion:
 * Código cliente depende da abstração CarteiraSelectionStrategy
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Strategy Pattern - Seleção de Carteira")
class CarteiraSelectionStrategyTest {

    @Test
    @DisplayName("Deve criar carteira conservadora com perfil CONSERVADOR")
    void deveExecutarEstrategiaConservadora() {
        // Arrange: Cria estratégia conservadora
        CarteiraSelectionStrategy strategy = new ConservadoraStrategy();
        BigDecimal valorInvestimento = new BigDecimal("100000.00");

        // Act: POLIMORFISMO - Chama método da interface
        Carteira carteira = strategy.selectCarteira(RiskProfile.CONSERVADOR, valorInvestimento);

        // Assert: Valida características da carteira conservadora
        assertThat(carteira).isNotNull();
        assertThat(carteira.getNome()).contains("Conservadora");
        assertThat(carteira.getPerfilRisco()).isEqualTo(RiskProfile.CONSERVADOR);
        assertThat(carteira.getRetornoEsperado()).isEqualTo(new BigDecimal("10.5"));
        assertThat(carteira.getRiscoEstimado()).isEqualTo(new BigDecimal("2.5"));
        assertThat(strategy.getNomeEstrategia()).isEqualTo("Conservadora");
    }

    @Test
    @DisplayName("Deve criar carteira moderada com perfil MODERADO")
    void deveExecutarEstrategiaModerada() {
        // Arrange: Cria estratégia moderada
        CarteiraSelectionStrategy strategy = new ModeradaStrategy();
        BigDecimal valorInvestimento = new BigDecimal("100000.00");

        // Act: POLIMORFISMO - Mesmo método, comportamento diferente
        Carteira carteira = strategy.selectCarteira(RiskProfile.MODERADO, valorInvestimento);

        // Assert: Valida características da carteira moderada
        assertThat(carteira).isNotNull();
        assertThat(carteira.getNome()).contains("Moderada");
        assertThat(carteira.getPerfilRisco()).isEqualTo(RiskProfile.MODERADO);
        assertThat(carteira.getRetornoEsperado()).isEqualTo(new BigDecimal("16.0"));
        assertThat(carteira.getRiscoEstimado()).isEqualTo(new BigDecimal("5.5"));
        assertThat(strategy.getNomeEstrategia()).isEqualTo("Moderada");
    }

    @Test
    @DisplayName("Deve criar carteira agressiva com perfil ARROJADO")
    void deveExecutarEstrategiaAgressiva() {
        // Arrange: Cria estratégia agressiva
        CarteiraSelectionStrategy strategy = new AgressivaStrategy();
        BigDecimal valorInvestimento = new BigDecimal("100000.00");

        // Act: POLIMORFISMO - Mesmo método, outro comportamento
        Carteira carteira = strategy.selectCarteira(RiskProfile.ARROJADO, valorInvestimento);

        // Assert: Valida características da carteira agressiva
        assertThat(carteira).isNotNull();
        assertThat(carteira.getNome()).contains("Agressiva");
        assertThat(carteira.getPerfilRisco()).isEqualTo(RiskProfile.ARROJADO);
        assertThat(carteira.getRetornoEsperado()).isEqualTo(new BigDecimal("25.0"));
        assertThat(carteira.getRiscoEstimado()).isEqualTo(new BigDecimal("12.5"));
        assertThat(strategy.getNomeEstrategia()).isEqualTo("Agressiva");
    }

    @Test
    @DisplayName("Deve demonstrar DESPACHO DINÂMICO - diferentes estratégias, mesma interface")
    void deveDemonstrarDespachoDinamico() {
        // Arrange: Array de estratégias diferentes - POLIMORFISMO
        CarteiraSelectionStrategy[] strategies = {
            new ConservadoraStrategy(),
            new ModeradaStrategy(),
            new AgressivaStrategy()
        };

        BigDecimal valorInvestimento = new BigDecimal("50000.00");

        // Act & Assert: DESPACHO DINÂMICO - método correto é chamado em runtime
        for (CarteiraSelectionStrategy strategy : strategies) {
            Carteira carteira = strategy.selectCarteira(null, valorInvestimento);

            assertThat(carteira).isNotNull();
            assertThat(carteira.getNome()).isNotBlank();
            assertThat(carteira.getRetornoEsperado()).isNotNull();
            assertThat(carteira.getRiscoEstimado()).isNotNull();
            assertThat(strategy.getNomeEstrategia()).isNotBlank();
            assertThat(strategy.getDescricaoEstrategia()).isNotBlank();
        }
    }

    @Test
    @DisplayName("Deve validar retornos crescentes: Conservadora < Moderada < Agressiva")
    void deveValidarRetornosCrescentes() {
        // Arrange
        ConservadoraStrategy conservadora = new ConservadoraStrategy();
        ModeradaStrategy moderada = new ModeradaStrategy();
        AgressivaStrategy agressiva = new AgressivaStrategy();
        BigDecimal valor = new BigDecimal("100000.00");

        // Act
        BigDecimal retornoConservador = conservadora.selectCarteira(RiskProfile.CONSERVADOR, valor).getRetornoEsperado();
        BigDecimal retornoModerado = moderada.selectCarteira(RiskProfile.MODERADO, valor).getRetornoEsperado();
        BigDecimal retornoAgressivo = agressiva.selectCarteira(RiskProfile.ARROJADO, valor).getRetornoEsperado();

        // Assert: Retornos devem crescer com o risco
        assertThat(retornoConservador).isLessThan(retornoModerado);
        assertThat(retornoModerado).isLessThan(retornoAgressivo);
    }

    @Test
    @DisplayName("Deve validar riscos crescentes: Conservadora < Moderada < Agressiva")
    void deveValidarRiscosCrescentes() {
        // Arrange
        ConservadoraStrategy conservadora = new ConservadoraStrategy();
        ModeradaStrategy moderada = new ModeradaStrategy();
        AgressivaStrategy agressiva = new AgressivaStrategy();
        BigDecimal valor = new BigDecimal("100000.00");

        // Act
        BigDecimal riscoConservador = conservadora.selectCarteira(RiskProfile.CONSERVADOR, valor).getRiscoEstimado();
        BigDecimal riscoModerado = moderada.selectCarteira(RiskProfile.MODERADO, valor).getRiscoEstimado();
        BigDecimal riscoAgressivo = agressiva.selectCarteira(RiskProfile.ARROJADO, valor).getRiscoEstimado();

        // Assert: Riscos devem crescer com o retorno
        assertThat(riscoConservador).isLessThan(riscoModerado);
        assertThat(riscoModerado).isLessThan(riscoAgressivo);
    }
}
