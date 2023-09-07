package com.api.exchange.service;

import com.api.exchange.model.ExchangeRates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class ExchangeRateService {

    @Value("${app.exchange.url}")
    private String baseUrl;

    @Autowired
    public WebClient webClient;

    public ExchangeRates getExchangeRates() {
        return webClient.get()
                .uri(baseUrl)
                .retrieve()
                .bodyToFlux(ExchangeRates.class)
                .doOnError(t -> log.info("Error fetching exchange rates"))
                .blockFirst();
    }
}
