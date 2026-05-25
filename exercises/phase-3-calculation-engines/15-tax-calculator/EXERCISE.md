# Exercise 15 — Tax Calculator

## Scenario

Build a UK income tax and National Insurance calculator for the 2024/25 tax year. The engine must handle the personal allowance taper, marriage allowance, pension contributions, and the full tax and NI band structure.

## Models

| Class | Key Fields |
|-------|-----------|
| `TaxInput` | annualIncomeGBP, taxYear, pensionContributionGBP, marriageAllowanceTransferred |
| `TaxBandResult` | bandName, ratePercent, incomeInBandGBP, taxInBandGBP |
| `TaxBreakdown` | personalAllowance, taxableIncome, bands, totalIncomeTaxGBP, totalNIGBP, totalDeductionsGBP, takeHomeGBP |

## Method Signature

```java
public TaxBreakdown calculate(TaxInput input)
```

## Criteria

1. Base personal allowance is £12,570. If `marriageAllowanceTransferred` is true, add £1,260.
2. `adjustedIncome = annualIncomeGBP - pensionContributionGBP`.
3. Taper: for every £2 of `adjustedIncome` above £100,000, reduce `personalAllowance` by £1 (minimum £0).
4. `taxableIncome = max(0, adjustedIncome - personalAllowance)`.
5. Income tax bands (applied to `taxableIncome`): Basic 20% on £0–£37,700; Higher 40% on £37,701–£125,140; Additional 45% above £125,140.
6. NI is calculated on `annualIncomeGBP` (not adjusted): 0% below £12,570; 8% on £12,570–£50,270; 2% above £50,270.
7. `totalDeductionsGBP = totalIncomeTaxGBP + totalNIGBP`.
8. `takeHomeGBP = annualIncomeGBP - totalIncomeTaxGBP - totalNIGBP - pensionContributionGBP`.
9. The `bands` list is populated with a `TaxBandResult` for each band that has income in it.

## Running Tests

```bash
cd 15-tax-calculator
mvn test
```

<details>
<summary>Hint</summary>

Compute the personal allowance first (base + marriage allowance, then taper). Then compute `taxableIncome`. For each band, take `min(bandTop, taxableIncome) - bandBottom` clamped to 0 as the income in band, and multiply by the rate. For NI, apply the same band approach on the raw income. Sum everything at the end.

</details>
