package com.api.exchange.controller;

import com.api.exchange.model.ExchangeRates;
import com.api.exchange.service.ExchangeRateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ExchangeRatesController.class})
class ExchangeRatesControllerTest {

    private static final String RON = "RON";
    private static final String EUR = "EUR";
    private static final String USD = "USD";
    private static final String INVALID_CURRENCY = "mm";
    private static final BigDecimal VALUE = BigDecimal.TEN;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExchangeRateService exchangeRateService;

    @Test
    void getExchangeRates() throws Exception {
        Map<String, BigDecimal> rates = Map.of(EUR, BigDecimal.TEN,
                USD, BigDecimal.valueOf(5));
        var exchangeRates = new ExchangeRates();
        exchangeRates.setRates(rates);

        when(exchangeRateService.getExchangeRates(RON)).thenReturn(exchangeRates);

        mockMvc.perform(get("/v1/exchange/{currency}", RON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json("{\"EUR\": 10, \"USD\": 5}"))
                .andDo(print());
    }

    @Test
    void getExchangeRates_badRequest() throws Exception {
        mockMvc.perform(get("/v1/exchange/{currency}", INVALID_CURRENCY))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void getExchangeRate() throws Exception {
        Map<String, BigDecimal> rates = Map.of(EUR, BigDecimal.TEN);
        var exchangeRates = new ExchangeRates();
        exchangeRates.setRates(rates);

        when(exchangeRateService.getExchangeRate(RON, EUR)).thenReturn(exchangeRates);

        mockMvc.perform(get("/v1/exchange/from/{currency}/to/{symbol}", RON, EUR))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json("10"))
                .andDo(print());
    }

    @Test
    void getExchangeRate_noCurrency_badRequest() throws Exception {
        mockMvc.perform(get("/v1/exchange/from/{currency}/to/{symbol}", INVALID_CURRENCY, EUR))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void getExchangeRate_noSymbol_badRequest() throws Exception {
        mockMvc.perform(get("/v1/exchange/from/{currency}/to/{symbol}", RON, INVALID_CURRENCY))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void getValueConversion() throws Exception {
        when(exchangeRateService.getValueConversion(VALUE, EUR, RON)).thenReturn(BigDecimal.valueOf(50));

        mockMvc.perform(get("/v1/exchange/value/{value}", VALUE)
                        .queryParam("currency", EUR)
                        .queryParam("symbol", RON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json("50"))
                .andDo(print());
    }

    @Test
    void getValueConversion_noCurrency_badRequest() throws Exception {
        mockMvc.perform(get("/v1/exchange/value/{value}", VALUE)
                        .queryParam("symbol", RON))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void getValueConversion_noSymbol_badRequest() throws Exception {
        mockMvc.perform(get("/v1/exchange/value/{value}", VALUE)
                        .queryParam("currency", RON))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void getValueConversion_noValue_notFound() throws Exception {
        mockMvc.perform(get("/v1/exchange/value/")
                        .queryParam("currency", RON)
                        .queryParam("symbol", RON))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void getValuesConversion() throws Exception {
        String[] symbols = {EUR, USD};
        var values = Map.of(EUR, BigDecimal.valueOf(100),
                USD, BigDecimal.valueOf(50));
        when(exchangeRateService.getValuesConversion(VALUE, RON, symbols)).thenReturn(values);

        mockMvc.perform(get("/v1/exchange/values/{value}", VALUE)
                        .queryParam("currency", RON)
                        .queryParam("symbols", symbols))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json("{\"EUR\": 100, \"USD\": 50}"))
                .andDo(print());
    }

    @Test
    void getValuesConversion_noCurrency_badRequest() throws Exception {
        String[] symbols = {EUR, USD};

        mockMvc.perform(get("/v1/exchange/values/{value}", VALUE)
                        .queryParam("symbols", symbols))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void getValuesConversion_noSymbols_badRequest() throws Exception {
        mockMvc.perform(get("/v1/exchange/values/{value}", VALUE)
                        .queryParam("currency", RON))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }
}
