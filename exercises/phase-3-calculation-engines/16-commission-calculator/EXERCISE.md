# Exercise 16 — Commission Calculator

## Scenario

A SaaS sales organisation needs a commission engine. Each month, sales records are processed per salesperson to calculate tiered commission with product-line multipliers, target achievement tracking, and a minimum achievement threshold.

## Models

| Class | Key Fields |
|-------|-----------|
| `ProductLine` | enum: STANDARD, PREMIUM, ENTERPRISE |
| `SaleRecord` | salespersonId, saleDate, dealValueGBP, productLine |
| `CommissionResult` | salespersonId, totalSalesGBP, targetGBP, achievementPercent, commissionGBP, tierReached |

## Method Signature

```java
public List<CommissionResult> calculate(List<SaleRecord> sales, Map<String, BigDecimal> targets, YearMonth period)
```

## Criteria

1. Only sales whose `saleDate` falls within the specified `YearMonth` period are counted.
2. Commission tiers (applied to `totalSalesGBP` like tax bands): 5% on first £10,000; 8% on £10,001–£25,000; 12% above £25,000.
3. PREMIUM deals multiply their commission contribution by 1.5.
4. ENTERPRISE deals multiply their commission contribution by 2.0.
5. Compute commission per deal using a running total to determine which tier rate applies to each deal's value; apply the product-line multiplier per deal.
6. `achievementPercent = (totalSalesGBP / targetGBP) × 100`, rounded to 2dp.
7. If `achievementPercent < 50`, set `commissionGBP = 0`.
8. `tierReached`: "Tier 1" if `totalSalesGBP ≤ £10,000`; "Tier 2" if `≤ £25,000`; "Tier 3" above £25,000.
9. If a salesperson has no entry in the `targets` map, use `BigDecimal.ZERO` as their target.
10. Produce one `CommissionResult` per salesperson who had any sales in the period.

## Running Tests

```bash
cd 16-commission-calculator
mvn test
```

<details>
<summary>Hint</summary>

Group sales by salesperson ID first. For each salesperson, filter to the period, sum `totalSalesGBP`, then compute commission per deal by sorting deals and tracking a `runningTotal` to determine how much of each deal falls in which tier band. Multiply by the product-line multiplier before adding to the total commission. Finally apply the achievement threshold check.

</details>
