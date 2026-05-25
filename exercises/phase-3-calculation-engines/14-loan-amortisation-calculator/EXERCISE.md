# Exercise 14 — Loan Amortisation Calculator

## Scenario

A fintech company needs a loan amortisation engine. Given a loan principal, annual interest rate, term in months, and start date, generate the full repayment schedule showing how each monthly payment is split between interest and principal repayment, until the balance reaches zero.

## Models

| Class | Key Fields |
|-------|-----------|
| `LoanDetails` | principalGBP, annualInterestRatePercent, termMonths, startDate |
| `RepaymentRow` | month, paymentDate, openingBalanceGBP, interestGBP, principalRepaidGBP, closingBalanceGBP |
| `AmortisationSchedule` | rows, monthlyPaymentGBP, totalInterestGBP, totalPaidGBP |

## Method Signature

```java
public AmortisationSchedule calculate(LoanDetails loan)
```

## Criteria

1. `monthlyRate = annualInterestRatePercent / 12 / 100`
2. `monthlyPayment = principal × (monthlyRate × (1 + monthlyRate)^term) / ((1 + monthlyRate)^term - 1)` — use `Math.pow` for the exponent.
3. The schedule has exactly `termMonths` rows.
4. Row 1's `openingBalanceGBP` equals the loan principal.
5. Each row: `interestGBP = openingBalance × monthlyRate` (rounded to 2dp).
6. Each row: `principalRepaidGBP = monthlyPayment - interestGBP` (rounded to 2dp).
7. Each row: `closingBalanceGBP = openingBalance - principalRepaidGBP`.
8. The last row is adjusted so `closingBalanceGBP` is exactly `0.00`.
9. `paymentDate` for row n is `startDate.plusMonths(n)`.
10. `month` numbers run 1 through termMonths.
11. `totalInterestGBP` equals the sum of all `interestGBP` values.
12. `totalPaidGBP` equals the sum of all actual payments (interest + principal per row).

## Running Tests

```bash
cd 14-loan-amortisation-calculator
mvn test
```

<details>
<summary>Hint</summary>

Compute `monthlyPayment` once using the annuity formula (convert `BigDecimal` to `double` for `Math.pow`, then convert back and round). Iterate through each month: carry `openingBalance` forward from the previous row's `closingBalance`. For the last row, override `principalRepaidGBP = openingBalance` and set `closingBalance = 0.00` to eliminate rounding drift.

</details>
