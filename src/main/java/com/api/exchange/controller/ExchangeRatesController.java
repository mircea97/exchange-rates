package com.api.exchange.controller;

import com.api.exchange.model.ExchangeRates;
import com.api.exchange.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/exchange")
@Tag(name = "Exchange Rates Service")
@RequiredArgsConstructor
public class ExchangeRatesController {

    private final ExchangeRateService exchangeRateService;

    @GetMapping
    public ResponseEntity<ExchangeRates> getExchangeRates() {
        return ResponseEntity.ok(exchangeRateService.getExchangeRates());
    }
}
