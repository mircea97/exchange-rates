package com.api.exchange.service;

import com.api.exchange.model.ExchangeRates;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {

    private static final String URL = "https://test.com";
    private static final String RON = "RON";
    private static final String EUR = "EUR";
    private static final String USD = "USD";
    private static final BigDecimal VALUE = BigDecimal.TEN;

    @Mock
    private RestTemplate restTemplate;

    @Spy
    @InjectMocks
    private ExchangeRateService exchangeRateService;

    @Test
    void getExchangeRates() {
        // given
        var exchangeRates = new ExchangeRates();
        exchangeRates.setRates(Map.of(EUR, BigDecimal.valueOf(0.2)));
        when(restTemplate.getForObject(anyString(), eq(ExchangeRates.class), anyMap())).thenReturn(exchangeRates);

        // when
        var result = exchangeRateService.getExchangeRates(RON);

        // then
        assertThat(result).isEqualTo(exchangeRates);
    }

/*    @Test
    void getExchangeRate() {
        // given
        var exchangeRates = new ExchangeRates();
        exchangeRates.setRates(Map.of(EUR, BigDecimal.valueOf(0.2)));

        doReturn(URL).when(exchangeRateService).buildUrl();
        when(restTemplate.getForObject(eq(URL), eq(ExchangeRates.class), anyMap())).thenReturn(exchangeRates);

        var uriBuilder = mock(UriComponentsBuilder.class);

        // when
        try (MockedStatic<UriComponentsBuilder> uri = Mockito.mockStatic(UriComponentsBuilder.class)) {
            uri.when(UriComponentsBuilder::fromHttpUrl).thenReturn(uriBuilder);
            var result = exchangeRateService.getExchangeRate(RON, EUR);
        }

        // then
        assertThat(result).isEqualTo(exchangeRates);
    }*/

    @Test
    void getValueConversion() {
        // given
        var exchangeRates = new ExchangeRates();
        exchangeRates.setRates(Map.of(EUR, BigDecimal.valueOf(0.2)));
        doReturn(exchangeRates).when(exchangeRateService).getExchangeRate(RON, EUR);

        // when
        var result = exchangeRateService.getValueConversion(VALUE, RON, EUR);

        // then
        assertThat(result).isEqualTo(BigDecimal.valueOf(2.0));
    }

    @Test
    void getValuesConversion() {
        // given
        String[] symbols = {EUR, USD};
        var exchangeRates = new ExchangeRates();
        exchangeRates.setRates(Map.of(EUR, BigDecimal.valueOf(0.2),
                USD, BigDecimal.valueOf(0.22)));

        doReturn(URL).when(exchangeRateService).buildUrl();
        when(restTemplate.getForObject(eq(URL), eq(ExchangeRates.class), anyMap())).thenReturn(exchangeRates);

        // when
        var result = exchangeRateService.getValuesConversion(VALUE, RON, symbols);

        // then
        assertThat(result)
                .containsEntry(EUR, BigDecimal.valueOf(2.0))
                .containsEntry(USD, BigDecimal.valueOf(2.20).setScale(2, RoundingMode.CEILING));
    }
}
