# Exercise 2 — Invoice Validator

## Your Task
Implement the `validate()` method inside:
```
src/main/java/com/practice/service/InvoiceValidator.java
```
Do not modify any other file.

---

## Scenario
A finance system validates supplier invoices before they can be processed for payment.
When an invoice is submitted, the system must check it against the rules below.
If any rules are broken, the invoice is rejected and **all violations must be listed** — not just the first one found.

---

## The Models (already built for you)

**`LineItem`** — a single line on the invoice
| Field | Type | Description |
|---|---|---|
| description | String | What the line item is for |
| quantity | int | Number of units |
| unitPriceGBP | BigDecimal | Price per unit |
| vatRatePercent | int | VAT rate applied to this item |
| category | LineItemCategory | Category of the line item |

**`LineItemCategory`** — enum with values: `GOODS`, `SERVICES`, `SUBSCRIPTION`, `CONSULTING`

**`Invoice`** — the full invoice submitted by a supplier
| Field | Type | Description |
|---|---|---|
| supplierName | String | Name of the supplier |
| invoiceDate | LocalDate | Date the invoice was issued |
| paymentDueDate | LocalDate | Date payment is due by |
| lineItems | List\<LineItem\> | All line items on the invoice |

**`InvoiceValidationResult`** — what your method must return
- `InvoiceValidationResult.approved()` — use this when there are no violations
- `InvoiceValidationResult.rejected(violations)` — use this when there are one or more violations

**`Violation`** — a single rule breach, constructed with a message string

---

## Rules to Implement

### Rule 1 — Quantity
Every line item must have a quantity greater than zero.
- Violation message must contain: `"quantity"`
- A quantity of 0 or any negative number is a violation.

### Rule 2 — VAT Rate
The VAT rate on each line item must be exactly 0, 5, or 20.
- Violation message must contain: `"vat rate"`
- Any other value (e.g. 10, 15, 25) is a violation.

### Rule 3 — Category Price Limit
The unit price cannot exceed the cap for its category:
| Category | Cap |
|---|---|
| GOODS | £1,000 |
| SERVICES | £500 |
| SUBSCRIPTION | £200 |
| CONSULTING | £800 |
- Violation message must contain: `"limit"`
- A price exactly at the cap is fine. Only strictly exceeding it is a violation.

### Rule 4 — Payment Terms
The payment due date must not be more than 60 days after the invoice date.
- Violation message must contain: `"payment terms"`
- Exactly 60 days is fine. Only strictly more than 60 days is a violation.

### Rule 5 — Duplicate Line Items
Two line items with the same description **and** the same unit price are considered duplicates.
- Violation message must contain: `"duplicate"`
- Same description but different price = not a duplicate.
- Different description but same price = not a duplicate.

---

## Important Rules (apply to all exercises)
- **Collect all violations.** Do not return early after the first one.
- **An amount exactly at a limit is not a violation.** Only strictly exceeding it is.
- Return `approved()` only when the violations list is empty.
- Return `rejected(violations)` when there is one or more violation.

---

## Running the Tests
```bash
mvn test
```
There are 20 tests. They all fail until your implementation is correct.
You are done when all 20 pass.

---

## Hint (read only if stuck)
<details>
<summary>Click to reveal</summary>

Structure your method like this:
1. Create an empty `List<Violation>` at the top.
2. Write one private method per rule. Each one adds to the list if the rule is broken.
3. Call all five methods.
4. At the end, if the list is empty return `approved()`, otherwise return `rejected(violations)`.

For Rule 5 (duplicates), iterate through the list and compare each item against every item that comes after it. Two nested loops work fine here.

For Rule 3 (price caps), a `Map<LineItemCategory, BigDecimal>` of the caps makes the check clean.

</details>
