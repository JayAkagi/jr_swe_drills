package com.practice.service;

import com.practice.model.ConversionRequest;
import com.practice.model.ConversionResult;
import com.practice.model.ExchangeRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyConverterTest {

    private CurrencyConverter converter;
    private static final LocalDate TODAY = LocalDate.of(2024, 6, 15);

    @BeforeEach
    void setUp() {
        converter = new CurrencyConverter();
    }

    private ExchangeRate rate(String from, String to, String rateStr, LocalDate from_, LocalDate until) {
        return new ExchangeRate(from, to, new BigDecimal(rateStr), from_, until);
    }

    @Test
    void shouldUseDirectValidRate_whenRateIsValidOnAsOfDate() {
        ExchangeRate er = rate("GBP", "USD", "1.27", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
        ConversionRequest req = new ConversionRequest(new BigDecimal("100.00"), "GBP", "USD", TODAY);
        ConversionResult result = converter.convert(req, List.of(er));
        assertEquals(0, new BigDecimal("127.00").compareTo(result.getConvertedAmount()));
        assertNull(result.getWarning());
    }

    @Test
    void shouldUseRate_whenAsOfDateIsExactlyValidFrom() {
        ExchangeRate er = rate("GBP", "EUR", "1.15", TODAY, LocalDate.of(2024, 12, 31));
        ConversionRequest req = new ConversionRequest(new BigDecimal("200.00"), "GBP", "EUR", TODAY);
        ConversionResult result = converter.convert(req, List.of(er));
        assertEquals(0, new BigDecimal("230.00").compareTo(result.getConvertedAmount()));
        assertNull(result.getWarning());
    }

    @Test
    void shouldUseRate_whenAsOfDateIsExactlyValidUntil() {
        ExchangeRate er = rate("GBP", "EUR", "1.15", LocalDate.of(2024, 1, 1), TODAY);
        ConversionRequest req = new ConversionRequest(new BigDecimal("200.00"), "GBP", "EUR", TODAY);
        ConversionResult result = converter.convert(req, List.of(er));
        assertEquals(0, new BigDecimal("230.00").compareTo(result.getConvertedAmount()));
        assertNull(result.getWarning());
    }

    @Test
    void shouldUseGBPPath_whenNoDirectRateButGBPHopAvailable() {
        ExchangeRate usdToGbp = rate("USD", "GBP", "0.80", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
        ExchangeRate gbpToEur = rate("GBP", "EUR", "1.15", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
        ConversionRequest req = new ConversionRequest(new BigDecimal("100.00"), "USD", "EUR", TODAY);
        ConversionResult result = converter.convert(req, List.of(usdToGbp, gbpToEur));
        BigDecimal combinedRate = new BigDecimal("0.80").multiply(new BigDecimal("1.15"));
        BigDecimal expectedAmount = new BigDecimal("100.00").multiply(combinedRate).setScale(2, java.math.RoundingMode.HALF_UP);
        assertEquals(0, expectedAmount.compareTo(result.getConvertedAmount()));
        assertNull(result.getWarning());
    }

    @Test
    void shouldUseExpiredDirectRate_whenNoValidRateExists() {
        ExchangeRate expired = rate("GBP", "USD", "1.25", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));
        ConversionRequest req = new ConversionRequest(new BigDecimal("100.00"), "GBP", "USD", TODAY);
        ConversionResult result = converter.convert(req, List.of(expired));
        assertEquals(0, new BigDecimal("125.00").compareTo(result.getConvertedAmount()));
        assertEquals("using expired rate", result.getWarning());
    }

    @Test
    void shouldThrowIllegalArgumentException_whenNoRateAtAllExists() {
        ConversionRequest req = new ConversionRequest(new BigDecimal("100.00"), "GBP", "JPY", TODAY);
        assertThrows(IllegalArgumentException.class, () -> converter.convert(req, List.of()));
    }

    @Test
    void shouldRoundConvertedAmountToTwoDecimalPlaces() {
        ExchangeRate er = rate("GBP", "USD", "1.27573", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
        ConversionRequest req = new ConversionRequest(new BigDecimal("100.00"), "GBP", "USD", TODAY);
        ConversionResult result = converter.convert(req, List.of(er));
        assertEquals(2, result.getConvertedAmount().scale());
    }

    @Test
    void shouldHaveNullWarning_whenDirectValidRateIsUsed() {
        ExchangeRate er = rate("EUR", "GBP", "0.86", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
        ConversionRequest req = new ConversionRequest(new BigDecimal("50.00"), "EUR", "GBP", TODAY);
        ConversionResult result = converter.convert(req, List.of(er));
        assertNull(result.getWarning());
    }

    @Test
    void shouldSetWarningToExpiredRate_whenExpiredRateIsUsed() {
        ExchangeRate expired = rate("EUR", "GBP", "0.86", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 6, 30));
        ConversionRequest req = new ConversionRequest(new BigDecimal("50.00"), "EUR", "GBP", TODAY);
        ConversionResult result = converter.convert(req, List.of(expired));
        assertEquals("using expired rate", result.getWarning());
    }

    @Test
    void shouldChooseMostRecentlyExpiredRate_whenMultipleExpiredRatesExist() {
        ExchangeRate older = rate("GBP", "USD", "1.20", LocalDate.of(2022, 1, 1), LocalDate.of(2022, 12, 31));
        ExchangeRate newer = rate("GBP", "USD", "1.25", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));
        ConversionRequest req = new ConversionRequest(new BigDecimal("100.00"), "GBP", "USD", TODAY);
        ConversionResult result = converter.convert(req, List.of(older, newer));
        assertEquals(0, new BigDecimal("125.00").compareTo(result.getConvertedAmount()));
    }

    @Test
    void shouldUseExpiredGBPPath_whenNoDirectOrValidGBPPathExists() {
        ExchangeRate usdToGbp = rate("USD", "GBP", "0.79", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));
        ExchangeRate gbpToEur = rate("GBP", "EUR", "1.14", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));
        ConversionRequest req = new ConversionRequest(new BigDecimal("100.00"), "USD", "EUR", TODAY);
        ConversionResult result = converter.convert(req, List.of(usdToGbp, gbpToEur));
        assertEquals("using expired rate", result.getWarning());
    }

    @Test
    void shouldReturnOriginalAmountInResult_whenConverting() {
        ExchangeRate er = rate("GBP", "USD", "1.27", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
        ConversionRequest req = new ConversionRequest(new BigDecimal("250.00"), "GBP", "USD", TODAY);
        ConversionResult result = converter.convert(req, List.of(er));
        assertEquals(0, new BigDecimal("250.00").compareTo(result.getOriginalAmount()));
    }
}
