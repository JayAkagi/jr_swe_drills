# Exercise 5 — Payroll Validator

## Your Task
Implement the `validate()` method inside:
```
src/main/java/com/practice/service/PayrollValidator.java
```
Do not modify any other file.

---

## Scenario
A finance system validates monthly payroll submissions before processing payments.
When a payroll batch is submitted, the system must check every entry against the rules below.
If any rules are broken, the submission is rejected and **all violations must be listed** — not just the first one found.

---

## The Models (already built for you)

**`PayrollEntry`** — a single employee's payroll for the period
| Field | Type | Description |
|---|---|---|
| employeeId | String | Unique identifier for the employee |
| employeeName | String | Full name |
| department | Department | The employee's department |
| basicSalaryGBP | BigDecimal | Monthly base salary |
| bonusGBP | BigDecimal | Monthly bonus payment |
| deductionsGBP | BigDecimal | Total deductions (pension, benefits, etc.) |
| payPeriod | YearMonth | The month this entry is for (e.g. 2024-01) |

**`Department`** — enum with values: `ENGINEERING`, `SALES`, `OPERATIONS`, `HR`, `FINANCE`

**`PayrollSubmission`** — the full batch submitted by the payroll team
| Field | Type | Description |
|---|---|---|
| submittedBy | String | Who submitted the batch |
| submissionDate | LocalDate | When the batch was submitted |
| entries | List\<PayrollEntry\> | All employee entries in this batch |

**`PayrollValidationResult`** — what your method must return
- `PayrollValidationResult.approved()` — use this when there are no violations
- `PayrollValidationResult.rejected(violations)` — use this when there are one or more violations

**`Violation`** — a single rule breach, constructed with a message string

---

## Rules to Implement

### Rule 1 — Salary Cap
An employee's basic salary cannot exceed the cap for their department:
| Department | Cap |
|---|---|
| ENGINEERING | £8,000 |
| SALES | £7,000 |
| OPERATIONS | £6,000 |
| HR | £5,500 |
| FINANCE | £7,500 |
- Violation message must contain: `"salary cap"`
- A salary exactly at the cap is fine. Only strictly exceeding it is a violation.

### Rule 2 — Bonus Limit
A bonus cannot exceed 30% of the employee's basic salary.
- Violation message must contain: `"bonus limit"`
- Exactly 30% is fine. Only strictly more than 30% is a violation.

### Rule 3 — Deductions
Deductions cannot exceed 40% of the employee's basic salary.
- Violation message must contain: `"deductions"`
- Exactly 40% is fine. Only strictly more than 40% is a violation.

### Rule 4 — Duplicate Employee
The same `employeeId` cannot appear more than once in the submission.
- Violation message must contain: `"duplicate employee"`
- Flag if any ID appears two or more times.

### Rule 5 — Pay Period
The `submissionDate` must fall within the same calendar month as the entry's `payPeriod`.
- Violation message must contain: `"pay period"`
- Apply per entry — an entry with a payPeriod that doesn't match the submission month is a violation.

### Rule 6 — Total Payroll
The total payroll across all entries cannot exceed £500,000.
- Total = sum of `(basicSalary + bonus − deductions)` for every entry.
- Violation message must contain: `"total payroll"`
- Exactly £500,000 is fine. Only strictly more than £500,000 is a violation.

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
There are 14 tests. They all fail until your implementation is correct.
You are done when all 14 pass.

---

## Hint (read only if stuck)
<details>
<summary>Click to reveal</summary>

Use a `Map<Department, BigDecimal>` for the salary caps in Rule 1.

For **Rule 2**, use BigDecimal arithmetic: `bonus.compareTo(basicSalary.multiply(new BigDecimal("0.30"))) > 0`.

For **Rule 4**, track seen IDs in a `Set<String>`. If `set.add(id)` returns `false`, the ID is a duplicate.

For **Rule 5**, check `YearMonth.from(submission.getSubmissionDate()).equals(entry.getPayPeriod())`.

For **Rule 6**, accumulate the total with `BigDecimal` across all entries: `total = total.add(basic).add(bonus).subtract(deductions)`, then compare against `new BigDecimal("500000")`.

</details>
