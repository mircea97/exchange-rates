package com.api.exchange.controller;

import com.api.exchange.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/v1/exchange")
@Tag(name = "Exchange Rates Service")
@RequiredArgsConstructor
public class ExchangeRatesController {

    private final ExchangeRateService exchangeRateService;

    @GetMapping("/{currency}")
    public ResponseEntity<Map<String, BigDecimal>> getAllExchangeRates(@PathVariable String currency) {
        final var exchangeRates = exchangeRateService.getAllExchangeRates(currency);
        return ResponseEntity.ok(exchangeRates.getRates());
    }

    @GetMapping("/from/{baseCurrency}/to/{exchangeCurrency}")
    public ResponseEntity<BigDecimal> getExchangeRates(@PathVariable String baseCurrency, @PathVariable String exchangeCurrency) {
        final var exchangeRate = exchangeRateService.getExchangeRate(baseCurrency, exchangeCurrency);
        return ResponseEntity.ok(exchangeRate.getRates().get(exchangeCurrency));
    }

    @GetMapping("/{value}")
    public ResponseEntity<BigDecimal> getExchangeRates(@PathVariable BigDecimal value,
                                                       @PathParam("baseCurrency") String baseCurrency,
                                                       @PathParam("exchangeCurrency") String exchangeCurrency) {
        return ResponseEntity.ok(exchangeRateService.getValueConversion(value, baseCurrency, exchangeCurrency));
    }
}
