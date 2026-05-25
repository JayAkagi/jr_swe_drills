# Exercise 3 — Timesheet Validator

## Your Task
Implement the `validate()` method inside:
```
src/main/java/com/practice/service/TimesheetValidator.java
```
Do not modify any other file.

---

## Scenario
A payroll system validates employee timesheets before processing them.
When a timesheet is submitted, the system must check it against the rules below.
If any rules are broken, the timesheet is rejected and **all violations must be listed** — not just the first one found.

---

## The Models (already built for you)

**`TimeEntry`** — a single logged block of time
| Field | Type | Description |
|---|---|---|
| date | LocalDate | The calendar day this entry covers |
| hoursWorked | double | Number of hours logged |
| projectCode | String | The project this time was spent on |
| entryType | EntryType | Category of the time entry |

**`EntryType`** — enum with values: `REGULAR`, `OVERTIME`, `LEAVE`

**`Timesheet`** — the full timesheet submitted by an employee
| Field | Type | Description |
|---|---|---|
| employeeName | String | Name of the employee |
| weekStartDate | LocalDate | The Monday the week begins (week ends on weekStartDate + 6) |
| submissionDate | LocalDate | When the timesheet was submitted |
| entries | List\<TimeEntry\> | All time entries for the week |

**`TimesheetValidationResult`** — what your method must return
- `TimesheetValidationResult.approved()` — use this when there are no violations
- `TimesheetValidationResult.rejected(violations)` — use this when there are one or more violations

**`Violation`** — a single rule breach, constructed with a message string

---

## Rules to Implement

### Rule 1 — Daily Hours
Total hours worked across all entries for a single day cannot exceed 12.
- Violation message must contain: `"daily hours"`
- Exactly 12 hours is fine. Only strictly more than 12 is a violation.
- Apply this check per calendar day, summing all entry types.

### Rule 2 — Overtime Eligibility
An `OVERTIME` entry on a day is only valid if that day already has at least 8 hours of `REGULAR` time.
- Violation message must contain: `"overtime"`
- If a day has one or more OVERTIME entries but the sum of REGULAR hours that day is less than 8, it is a violation.

### Rule 3 — Leave Hours
`LEAVE` entries must be exactly 8 hours.
- Violation message must contain: `"leave hours"`
- Any LEAVE entry that is not exactly 8 hours is a violation.

### Rule 4 — Late Submission
The timesheet must be submitted within 7 days of the week end date.
- Violation message must contain: `"late submission"`
- Week end date = `weekStartDate + 6 days`.
- Submission exactly 7 days after the week end is fine. Only strictly more than 7 days is a violation.

### Rule 5 — Duplicate Project
The same `projectCode` cannot appear more than once on the same day.
- Violation message must contain: `"duplicate project"`
- The same code on different days is fine.

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
There are 13 tests. They all fail until your implementation is correct.
You are done when all 13 pass.

---

## Hint (read only if stuck)
<details>
<summary>Click to reveal</summary>

The key to this exercise is grouping entries by date first.

Build a `Map<LocalDate, List<TimeEntry>>` at the start of your method. Then loop over each date group to apply Rules 1, 2, and 5:
- **Rule 1:** sum `hoursWorked` across all entries in the group and compare to 12.
- **Rule 2:** if any entry in the group has type `OVERTIME`, check that the sum of `REGULAR` hours in that group is >= 8.
- **Rule 5:** collect `projectCode` values into a `Set<String>`. If `set.add(code)` returns `false`, you have a duplicate.

For **Rule 4**, compute `weekEndDate = weekStartDate.plusDays(6)`, then check that `submissionDate` is not after `weekEndDate.plusDays(7)`.

For **Rule 3**, iterate through all entries separately and check any with type `LEAVE`.

</details>
