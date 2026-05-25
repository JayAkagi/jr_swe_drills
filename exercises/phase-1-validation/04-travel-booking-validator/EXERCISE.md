# Exercise 4 — Travel Booking Validator

## Your Task
Implement the `validate()` method inside:
```
src/main/java/com/practice/service/TravelBookingValidator.java
```
Do not modify any other file.

---

## Scenario
A corporate travel system validates employee travel requests before approving them.
When a request is submitted, the system must check all bookings against the rules below.
If any rules are broken, the request is rejected and **all violations must be listed** — not just the first one found.

---

## The Models (already built for you)

**`Booking`** — a single travel booking
| Field | Type | Description |
|---|---|---|
| destination | String | Where the employee is travelling to |
| departureDate | LocalDate | First day of the booking |
| returnDate | LocalDate | Last day of the booking |
| travelClass | TravelClass | Class of travel |
| costGBP | BigDecimal | Total cost of this booking |
| bookingType | BookingType | Type of booking |
| managerApproved | boolean | Whether a manager has signed off |

**`TravelClass`** — enum with values: `ECONOMY`, `BUSINESS`, `FIRST`

**`BookingType`** — enum with values: `FLIGHT`, `HOTEL`, `TRAIN`, `CAR_RENTAL`

**`TravelRequest`** — the full request submitted by an employee
| Field | Type | Description |
|---|---|---|
| employeeName | String | Name of the employee |
| budgetGBP | BigDecimal | Total approved budget for the trip |
| bookings | List\<Booking\> | All bookings in the request |

**`BookingValidationResult`** — what your method must return
- `BookingValidationResult.approved()` — use this when there are no violations
- `BookingValidationResult.rejected(violations)` — use this when there are one or more violations

**`Violation`** — a single rule breach, constructed with a message string

---

## Rules to Implement

### Rule 1 — Class Not Permitted
`FIRST` class travel is not allowed under any circumstances.
- Violation message must contain: `"class not permitted"`
- Flag every FIRST class booking.

### Rule 2 — Approval Required
`BUSINESS` class travel requires `managerApproved = true`.
- Violation message must contain: `"approval required"`
- ECONOMY bookings do not require approval.

### Rule 3 — Booking Cost Limit
A single booking cannot exceed the cost limit for its type:
| Booking Type | Limit |
|---|---|
| FLIGHT | £800 |
| HOTEL | £200 |
| TRAIN | £300 |
| CAR_RENTAL | £150 |
- Violation message must contain: `"limit"`
- A cost exactly at the limit is fine. Only strictly exceeding it is a violation.

### Rule 4 — Budget
The total cost of all bookings cannot exceed the request's budget.
- Violation message must contain: `"budget"`
- Total exactly equal to budget is fine. Only strictly exceeding it is a violation.

### Rule 5 — Overlapping Bookings
Two bookings of the **same type** cannot have overlapping date ranges.
- Violation message must contain: `"overlap"`
- Two ranges [A, B] and [C, D] overlap when `A <= D AND C <= B`.
- Bookings of different types may overlap freely.

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
There are 15 tests. They all fail until your implementation is correct.
You are done when all 15 pass.

---

## Hint (read only if stuck)
<details>
<summary>Click to reveal</summary>

For **Rules 1–4**, a single loop over all bookings handles most checks. Use a `Map<BookingType, BigDecimal>` for the cost limits in Rule 3, and accumulate a running total for Rule 4.

For **Rule 5 (overlap)**, group bookings by type first using a `Map<BookingType, List<Booking>>`. For each group, compare every pair using two nested loops: for each pair (i, j) where j > i, check if `dep_i <= ret_j AND dep_j <= ret_i`. If true, add a violation.

</details>
