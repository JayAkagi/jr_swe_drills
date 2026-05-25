# Exercise 18 — Currency Converter

## Scenario

A treasury system needs to convert amounts between currencies using a database of exchange rates that have validity periods. The converter must apply a priority-based fallback strategy: prefer a current direct rate, then try a GBP cross-rate path, then fall back to the most recently expired rates with a warning.

## Models

| Class | Key Fields |
|-------|-----------|
| `ExchangeRate` | fromCurrency, toCurrency, rate, validFrom, validUntil |
| `ConversionRequest` | amount, fromCurrency, toCurrency, asOfDate |
| `ConversionResult` | originalAmount, fromCurrency, convertedAmount, toCurrency, rateUsed, rateDate, warning (nullable) |

## Method Signature

```java
public ConversionResult convert(ConversionRequest request, List<ExchangeRate> rates)
```

## Criteria (applied in priority order)

1. Find a direct rate where `validFrom <= asOfDate <= validUntil` — use it, `warning = null`.
2. Find a GBP cross-path: `fromCurrency→GBP` and `GBP→toCurrency` both valid on `asOfDate` — combined rate = `rate1 × rate2`, `warning = null`.
3. Find the most recently expired direct rate (highest `validUntil`) — use it, `warning = "using expired rate"`.
4. Find the most recently expired GBP cross-path — use combined rate, `warning = "using expired rate"`.
5. If no rate can be found at all, throw `IllegalArgumentException`.
6. `convertedAmount = amount × rateUsed`, rounded to 2dp with `RoundingMode.HALF_UP`.

## Running Tests

```bash
cd 18-currency-converter
mvn test
```

<details>
<summary>Hint</summary>

Write a helper method that searches the rate list for a direct match given a date validity check (either `validFrom <= date <= validUntil` for current, or just returning the highest `validUntil` for expired). Apply the four fallback steps in order, returning as soon as one succeeds. For GBP paths, you need two successful lookups: `fromCurrency→GBP` and `GBP→toCurrency`.

</details>
