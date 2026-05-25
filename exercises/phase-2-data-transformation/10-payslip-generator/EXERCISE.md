# Exercise 10 — Payslip Generator

## Your Task
Implement the `generate()` method inside:
```
src/main/java/com/practice/service/PayslipGenerator.java
```
Do not modify any other file.

---

## Scenario
Given an employee record and a pay period, generate a monthly payslip with gross pay, tax, national insurance, pension deduction, benefit deductions, and net pay — all calculated from the employee's annual salary and contribution settings.

---

## The Models (already built for you)

**`BenefitDeduction`** — a named monthly benefit cost
| Field | Type | Description |
|---|---|---|
| name | String | Name of the benefit (e.g. "Health Insurance") |
| monthlyAmountGBP | BigDecimal | Monthly cost in GBP |

**`Employee`** — the employee record
| Field | Type | Description |
|---|---|---|
| employeeId | String | Employee identifier |
| name | String | Full name |
| annualSalaryGBP | BigDecimal | Annual gross salary |
| taxCode | String | HMRC tax code (informational) |
| pensionContributionPercent | BigDecimal | Pension contribution as a percentage |
| benefitDeductions | List\<BenefitDeduction\> | Monthly benefit costs |

**`Payslip`** — the generated monthly payslip
| Field | Type | Description |
|---|---|---|
| employeeId | String | Copied from employee |
| name | String | Copied from employee |
| period | YearMonth | The pay period |
| grossPayGBP | BigDecimal | Monthly gross pay |
| incomeTaxGBP | BigDecimal | Income tax deduction |
| nationalInsuranceGBP | BigDecimal | NI deduction |
| pensionDeductionGBP | BigDecimal | Pension deduction |
| otherDeductions | List\<BenefitDeduction\> | Copied benefit deductions |
| netPayGBP | BigDecimal | Take-home pay |

---

## Method to Implement
`Payslip generate(Employee employee, YearMonth period)`

---

## Criteria
All monetary values must be rounded to 2 decimal places using `HALF_UP`.

1. `grossPay` = `annualSalaryGBP` / 12
2. **Income tax** (applied to monthly gross pay):
   - 0% on the first £1,048
   - 20% on the portion between £1,048 and £4,189
   - 40% on any amount above £4,189
3. **National Insurance** (applied to monthly gross pay):
   - 0% on the first £1,048
   - 12% on the portion between £1,048 and £4,189
   - 2% on any amount above £4,189
4. `pensionDeduction` = `grossPay` × `pensionContributionPercent` / 100
5. `netPay` = `grossPay` − `incomeTax` − `nationalInsurance` − `pensionDeduction` − sum of all `benefitDeductions`

---

## Running the Tests
```bash
mvn test
```
There are 12 tests. They all fail until your implementation is correct.
You are done when all 12 pass.

---

## Hint (read only if stuck)
<details>
<summary>Click to reveal</summary>
Define constants for the two bracket boundaries (1048 and 4189). For each band, compute `Math.max(0, Math.min(grossPay, upperBound) - lowerBound)` to get the taxable amount in that band. Multiply by the rate. Use `BigDecimal.ROUND_HALF_UP` (or `RoundingMode.HALF_UP`) with scale 2 throughout.
</details>
