# Exercise 7 — Sales Report Aggregator

## Your Task
Implement the `generate()` method inside:
```
src/main/java/com/practice/service/SalesReportGenerator.java
```
Do not modify any other file.

---

## Scenario
You receive a flat list of sales transactions from across multiple regions. Your job is to aggregate them into a structured report that groups sales by region, then by product within each region, computing totals at each level.

---

## The Models (already built for you)

**`Transaction`** — a single sale event
| Field | Type | Description |
|---|---|---|
| transactionId | String | Unique transaction ID |
| region | String | Sales region (e.g. "North") |
| productName | String | Name of product sold |
| quantitySold | int | Number of units sold |
| unitPriceGBP | BigDecimal | Price per unit in GBP |
| saleDate | LocalDate | Date of sale |

**`ProductSummary`** — aggregated data for one product in one region
| Field | Type | Description |
|---|---|---|
| productName | String | Product name |
| totalQuantity | int | Sum of all quantitySold |
| totalRevenueGBP | BigDecimal | Sum of quantitySold × unitPriceGBP |

**`RegionReport`** — aggregated data for one region
| Field | Type | Description |
|---|---|---|
| region | String | Region name |
| products | List\<ProductSummary\> | Product summaries sorted alphabetically |
| totalRevenueGBP | BigDecimal | Sum of all product revenues in region |

**`SalesReport`** — top-level report
| Field | Type | Description |
|---|---|---|
| reportDate | LocalDate | Date the report was generated |
| regions | List\<RegionReport\> | Region reports sorted alphabetically |
| grandTotalRevenueGBP | BigDecimal | Sum of all region totals |

---

## Method to Implement
`SalesReport generate(List<Transaction> transactions, LocalDate reportDate)`

---

## Criteria
1. Transactions are grouped by region, then by product within each region.
2. Each `ProductSummary`: `totalQuantity` = sum of `quantitySold`; `totalRevenueGBP` = sum of (`quantitySold` × `unitPriceGBP`) across all transactions for that product.
3. Each `RegionReport.totalRevenueGBP` = sum of its `ProductSummary` revenues.
4. `grandTotalRevenueGBP` = sum of all region totals.
5. Regions are sorted alphabetically; products within each region are sorted alphabetically.
6. An empty transaction list returns a report with no regions and a grand total of zero.

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
Use `Collectors.groupingBy` twice — first by region, then by productName. For revenue per transaction, multiply `quantitySold` by `unitPriceGBP` using `BigDecimal.valueOf(quantitySold).multiply(unitPriceGBP)`. Sort the resulting maps by key before building the result objects.
</details>
