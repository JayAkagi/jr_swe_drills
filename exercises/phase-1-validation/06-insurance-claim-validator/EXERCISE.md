# Exercise 6 — Insurance Claim Validator

## Your Task
Implement the `validate()` method inside:
```
src/main/java/com/practice/service/InsuranceClaimValidator.java
```
Do not modify any other file.

This is the capstone exercise for Phase 1. It brings together every validation pattern you have practised so far.

---

## Scenario
An insurance system validates claims before they can be processed for payment.
When a claim is submitted, the system must check it against the rules below.
If any rules are broken, the claim is rejected and **all violations must be listed** — not just the first one found.

---

## The Models (already built for you)

**`ClaimItem`** — a single item on the claim
| Field | Type | Description |
|---|---|---|
| description | String | What the item is |
| claimAmountGBP | BigDecimal | Amount being claimed for this item |
| itemAgeYears | int | Age of the item in years |
| replacementOrRepair | RepairType | Whether the claim is for replacement or repair |
| evidenceAttached | boolean | Whether supporting evidence has been provided |

**`RepairType`** — enum with values: `REPLACEMENT`, `REPAIR`

**`ClaimType`** — enum with values: `HOME`, `VEHICLE`, `TRAVEL`, `HEALTH`

**`Claim`** — the full claim submitted by the claimant
| Field | Type | Description |
|---|---|---|
| policyNumber | String | The policy reference |
| claimantName | String | Name of the person claiming |
| incidentDate | LocalDate | When the incident occurred |
| submissionDate | LocalDate | When the claim was submitted |
| claimType | ClaimType | Type of insurance policy |
| items | List\<ClaimItem\> | All items on the claim |
| excessGBP | BigDecimal | The policy excess to be deducted |

**`ClaimValidationResult`** — what your method must return
- `ClaimValidationResult.approved()` — use this when there are no violations
- `ClaimValidationResult.rejected(violations)` — use this when there are one or more violations

**`Violation`** — a single rule breach, constructed with a message string

---

## Rules to Implement

### Rule 1 — Claim Too Old
The claim must be submitted within 180 days of the incident date.
- Violation message must contain: `"claim too old"`
- Exactly 180 days is fine. Only strictly more than 180 days is a violation.

### Rule 2 — Evidence Required
Any item with a claim amount **strictly over £100** must have evidence attached.
- Violation message must contain: `"evidence required"`
- Exactly £100 is fine. Only strictly above £100 triggers this rule.
- Flag each item individually.

### Rule 3 — Policy Limit
The total claim amount (sum of all item amounts) cannot exceed the policy limit for the claim type:
| Claim Type | Limit |
|---|---|
| HOME | £50,000 |
| VEHICLE | £20,000 |
| TRAVEL | £5,000 |
| HEALTH | £10,000 |
- Violation message must contain: `"policy limit"`
- A total exactly at the limit is fine. Only strictly exceeding it is a violation.

### Rule 4 — Replacement Not Eligible
Items older than 5 years (i.e. `itemAgeYears > 5`) are only eligible for `REPAIR`, not `REPLACEMENT`.
- Violation message must contain: `"replacement not eligible"`
- An item aged exactly 5 years may still be claimed as REPLACEMENT.
- Flag each ineligible item individually.

### Rule 5 — Duplicate Items
Two items with the same description **and** the same claim amount are considered duplicates.
- Violation message must contain: `"duplicate"`
- Same description but different amount = not a duplicate.

### Rule 6 — Below Excess
The net claim (total claim amount minus the policy excess) must be greater than zero.
- Net = sum of all `claimAmountGBP` values minus `excessGBP`.
- Violation message must contain: `"below excess"`
- Net of exactly zero is a violation — it must be strictly greater than zero.

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
There are 17 tests. They all fail until your implementation is correct.
You are done when all 17 pass.

---

## Hint (read only if stuck)
<details>
<summary>Click to reveal</summary>

Use `ChronoUnit.DAYS.between(claim.getIncidentDate(), claim.getSubmissionDate())` for Rule 1.

For **Rule 3**, use a `Map<ClaimType, BigDecimal>` of the policy limits and sum `claimAmountGBP` across all items.

For **Rule 5** (duplicates), use two nested loops comparing each item against every item that comes after it (same pattern as Exercise 2).

For **Rule 6**, compute the total first (you already need it for Rule 3), then `total.subtract(claim.getExcessGBP()).compareTo(BigDecimal.ZERO) <= 0` is a violation.

Structure your method as one private method per rule, each accepting the violations list, then call them all in sequence.

</details>
