package com.api.exchange.service;

import com.api.exchange.model.ExchangeRates;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeRateService {

    private static final String BASE_CURRENCY_PARAM = "base";
    private static final String SYMBOLS_PARAM = "symbols";
    private static final String CURRENCY = "currency";
    private static final String CURRENCY_PLACEHOLDER = "{currency}";

    @Value("${app.exchange.url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public ExchangeRates getExchangeRates(final String currency) {
        final var urlTemplate = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam(BASE_CURRENCY_PARAM, CURRENCY_PLACEHOLDER)
                .build()
                .toUriString();
        return restTemplate.getForObject(urlTemplate, ExchangeRates.class, Map.of(CURRENCY, currency));
    }

    public ExchangeRates getExchangeRate(final String currency, final String symbol) {
        final var params = Map.of(CURRENCY, currency, SYMBOLS_PARAM, symbol);
        return restTemplate.getForObject(buildUrl(), ExchangeRates.class, params);
    }

    public BigDecimal getValueConversion(final BigDecimal value, final String currency, final String symbol) {
        final var rate = getExchangeRate(currency, symbol).getRates().get(symbol);
        return value.multiply(rate);
    }

    public Map<String, BigDecimal> getValuesConversion(final BigDecimal value, final String currency, final String[] symbols) {
        final var params = Map.of(CURRENCY, currency,
                SYMBOLS_PARAM, String.join(",", symbols));

        final var exchangeRates = restTemplate.getForObject(buildUrl(), ExchangeRates.class, params);

        return Objects.requireNonNull(exchangeRates).getRates()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().multiply(value)));
    }

    private String buildUrl() {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam(BASE_CURRENCY_PARAM, CURRENCY_PLACEHOLDER)
                .queryParam(SYMBOLS_PARAM, "{symbols}")
                .build()
                .toUriString();
    }
}
