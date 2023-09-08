package com.api.exchange.controller;

import com.api.exchange.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/v1/exchange")
@Tag(name = "Exchange Rates Service")
@RequiredArgsConstructor
public class ExchangeRatesController {

    private final ExchangeRateService exchangeRateService;

    @GetMapping("/{currency:[A-Z]{3}}")
    public ResponseEntity<Map<String, BigDecimal>> getExchangeRates(@PathVariable String currency) {
        final var exchangeRates = exchangeRateService.getExchangeRates(currency);
        return ResponseEntity.ok(exchangeRates.getRates());
    }

    @GetMapping("/from/{currency:[A-Z]{3}}/to/{symbol:[A-Z]{3}}")
    public ResponseEntity<BigDecimal> getExchangeRate(@PathVariable String currency, @PathVariable String symbol) {
        final var exchangeRate = exchangeRateService.getExchangeRate(currency, symbol);
        return ResponseEntity.ok(exchangeRate.getRates().get(symbol));
    }

    @GetMapping("/value/{value}")
    public ResponseEntity<BigDecimal> getValueConversion(@PathVariable BigDecimal value,
                                                         @RequestParam("currency")
                                                         @Size(min = 3, max = 3, message = "Invalid currency") String currency,
                                                         @RequestParam("symbol")
                                                         @Size(min = 3, max = 3, message = "Invalid symbol") String symbol) {
        return ResponseEntity.ok(exchangeRateService.getValueConversion(value, currency, symbol));
    }

    @GetMapping("/values/{value}")
    public ResponseEntity<Map<String, BigDecimal>> getValuesConversion(@PathVariable BigDecimal value,
                                                                       @RequestParam("currency")
                                                                       @Size(min = 3, max = 3, message = "Invalid currency") String currency,
                                                                       @RequestParam("symbols") String[] symbols) {
        return ResponseEntity.ok(exchangeRateService.getValuesConversion(value, currency, symbols));
    }
}
