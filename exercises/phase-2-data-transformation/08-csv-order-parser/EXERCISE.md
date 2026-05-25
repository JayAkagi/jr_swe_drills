# Exercise 8 — CSV Order Parser

## Your Task
Implement the `parse()` method inside:
```
src/main/java/com/practice/service/OrderParser.java
```
Do not modify any other file.

---

## Scenario
You receive raw CSV text representing customer orders. Some rows may be malformed. Your job is to parse the content, collecting valid `Order` objects and recording `ParseError` details for any row that cannot be parsed.

---

## The Models (already built for you)

**`Order`** — a successfully parsed order row
| Field | Type | Description |
|---|---|---|
| orderId | String | Order identifier |
| customerName | String | Customer full name |
| productCode | String | Product code |
| quantity | int | Number of items ordered |
| priceGBP | BigDecimal | Price in GBP |
| orderDate | LocalDate | Date of order |

**`ParseError`** — a row that could not be parsed
| Field | Type | Description |
|---|---|---|
| rowNumber | int | 1-based row number (header = 1) |
| rawLine | String | The original unparsed line |
| reason | String | Human-readable explanation of the failure |

**`ParseResult`** — the combined output
| Field | Type | Description |
|---|---|---|
| successful | List\<Order\> | All successfully parsed orders |
| errors | List\<ParseError\> | All rows that failed parsing |

---

## Row Format
```
orderId,customerName,productCode,quantity,priceGBP,orderDate
```
Example:
```
ORD001,John Smith,PROD-A,2,19.99,2024-03-15
```

---

## Method to Implement
`ParseResult parse(String csvContent)`

---

## Criteria
1. The first row (header) is always skipped.
2. A row with the wrong number of columns produces a `ParseError` with a reason containing `"invalid format"`.
3. A non-numeric quantity or price produces a `ParseError` with a reason containing `"invalid number"`.
4. A quantity of zero or less produces a `ParseError` with a reason containing `"invalid quantity"`.
5. An unparseable date produces a `ParseError` with a reason containing `"invalid date"`.
6. A row that passes all checks produces an `Order`.
7. Row numbering: header = 1, first data row = 2. Row numbers must be correct for all errors.

---

## Running the Tests
```bash
mvn test
```
There are 14 tests. They all fail until your implementation is correct.
You are done when all 14 pass.

---

## Hint (read only if stuck)
<details>
<summary>Click to reveal</summary>
Split the content on newlines, then iterate with an index starting at 1. Skip index 0 (header). For each subsequent line, split on commas and check the length. Try parsing quantity with `Integer.parseInt` and price with `new BigDecimal(...)` inside try/catch blocks. Parse the date with `LocalDate.parse`. Collect successes and failures separately, then wrap both in a `ParseResult`.
</details>
