package com.api.exchange.service;

import com.api.exchange.model.ExchangeRates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

@Service
@Slf4j
public class ExchangeRateService {

    private static final String BASE_CURRENCY_PARAM = "base";
    private static final String SYMBOLS_PARAM = "symbols";

    @Autowired
    public WebClient webClient;

    public ExchangeRates getAllExchangeRates(final String currency) {
        return webClient.get()
                .uri(builder -> builder
                        .queryParam(BASE_CURRENCY_PARAM, currency)
                        .build())
                .retrieve()
                .bodyToMono(ExchangeRates.class)
                .doOnError(t -> log.info("Error fetching exchange rates for currency = {}", currency))
                .block();
    }

    public ExchangeRates getExchangeRate(final String baseCurrency, final String exchangeCurrency) {
        return webClient.get()
                .uri(builder -> builder
                        .queryParam(BASE_CURRENCY_PARAM, baseCurrency)
                        .queryParam(SYMBOLS_PARAM, exchangeCurrency)
                        .build())
                .retrieve()
                .bodyToMono(ExchangeRates.class)
                .doOnError(t -> log.info("Error fetching exchange rate from {} to {}", baseCurrency, exchangeCurrency))
                .block();
    }

    public BigDecimal getValueConversion(final BigDecimal value, final String baseCurrency, final String exchangeCurrency) {
        return BigDecimal.ONE;
    }
}
