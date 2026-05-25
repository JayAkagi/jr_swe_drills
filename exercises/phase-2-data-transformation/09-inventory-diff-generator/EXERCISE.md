# Exercise 9 — Inventory Diff Generator

## Your Task
Implement the `diff()` method inside:
```
src/main/java/com/practice/service/InventoryDiffGenerator.java
```
Do not modify any other file.

---

## Scenario
You are given two snapshots of a warehouse inventory — a previous state and a current state. Your job is to compute what changed: which items were added, which were removed, and which had their stock level or price modified.

---

## The Models (already built for you)

**`InventoryItem`** — a single product in inventory
| Field | Type | Description |
|---|---|---|
| sku | String | Unique stock-keeping unit identifier |
| productName | String | Human-readable product name |
| stockLevel | int | Current units in stock |
| unitPriceGBP | BigDecimal | Price per unit |

**`StockChange`** — an item present in both snapshots but with differences
| Field | Type | Description |
|---|---|---|
| sku | String | SKU of the changed item |
| productName | String | Product name (from current snapshot) |
| previousStock | int | Stock level in the previous snapshot |
| newStock | int | Stock level in the current snapshot |
| stockDelta | int | newStock − previousStock |
| previousPrice | BigDecimal | Unit price in previous snapshot |
| newPrice | BigDecimal | Unit price in current snapshot |
| priceChanged | boolean | true only when the price differs |

**`InventoryDiff`** — the complete diff result
| Field | Type | Description |
|---|---|---|
| added | List\<InventoryItem\> | Items present in current but not in previous |
| removed | List\<InventoryItem\> | Items present in previous but not in current |
| changed | List\<StockChange\> | Items in both snapshots with at least one difference |

---

## Method to Implement
`InventoryDiff diff(List<InventoryItem> previous, List<InventoryItem> current)`

---

## Criteria
1. An item present only in the current snapshot goes into `added`.
2. An item present only in the previous snapshot goes into `removed`.
3. An item present in both snapshots with a different stock level or different price goes into `changed` as a `StockChange`.
4. An item present in both snapshots with identical stock level and identical price is not included anywhere.
5. SKU is the unique identifier for matching items across snapshots.
6. `stockDelta` = `newStock` − `previousStock`.
7. `priceChanged` = `true` only when the unit price differs between snapshots.

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
Build a Map from SKU to item for both snapshots. Iterate current: if the SKU is not in previous, it is added. Iterate previous: if the SKU is not in current, it is removed. For SKUs present in both, compare stock and price — if either differs, build a StockChange. Use BigDecimal.compareTo for price equality.
</details>
