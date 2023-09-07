package com.api.exchange.service;

import com.api.exchange.config.RestTemplateConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Import(RestTemplateConfig.class)
class ExchangeRateServiceIntegrationTest {

    private static final String BASE_CURRENCY = "RON";
    private static final String EUR = "EUR";
    private static final BigDecimal VALUE = BigDecimal.TEN;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Test
    void getExchangeRatesForCurrency() {
        var result = exchangeRateService.getExchangeRates(BASE_CURRENCY);
        assertThat(result.getRates()).hasSize(169);
    }

    @Test
    void getExchangeRate() {
        var result = exchangeRateService.getExchangeRate(BASE_CURRENCY, EUR);
        assertThat(result.getRates()).hasSize(1);
        assertThat(result.getRates()).containsOnlyKeys(EUR);
    }

    @Test
    void getValueConversion() {
        var value = exchangeRateService.getValueConversion(VALUE, BASE_CURRENCY, EUR);
        assertTrue(value.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void getValuesConversion() {
        // given
        String[] symbols = {EUR, "USD", "HUF"};

        // when
        var result = exchangeRateService.getValuesConversion(VALUE, BASE_CURRENCY, symbols);

        // then
        assertThat(result)
                .hasSize(3)
                .containsOnlyKeys(symbols);
        assertThat(result.values()).allMatch(v -> v.compareTo(BigDecimal.ZERO) > 0);
    }
}
