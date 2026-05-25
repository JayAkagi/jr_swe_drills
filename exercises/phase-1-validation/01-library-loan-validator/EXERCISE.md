# Exercise 1 — Library Loan Validator

## Your Task
Implement the `validate()` method inside:
```
src/main/java/com/practice/service/LoanValidator.java
```
Do not modify any other file.

---

## Scenario
A library system validates book loan requests before approving them.
When a member submits a loan request, the system must check it against the rules below.
If any rules are broken, the request is rejected and **all violations must be listed** — not just the first one found.

---

## The Models (already built for you)

**`Book`** — a single book being requested
| Field | Type | Description |
|---|---|---|
| title | String | Name of the book |
| genre | BookGenre | Category of the book |
| replacementCostGBP | BigDecimal | Cost to replace if lost/damaged |
| isOverdue | boolean | Whether this book is currently overdue |

**`BookGenre`** — enum with values: `FICTION`, `NON_FICTION`, `REFERENCE`, `CHILDREN`

**`LoanRequest`** — the full request submitted by a member
| Field | Type | Description |
|---|---|---|
| memberName | String | Name of the member |
| requestedBooks | List\<Book\> | All books being requested |
| depositConfirmed | boolean | Whether the member has confirmed a deposit |

**`LoanValidationResult`** — what your method must return
- `LoanValidationResult.approved()` — use this when there are no violations
- `LoanValidationResult.rejected(violations)` — use this when there are one or more violations

**`Violation`** — a single rule breach, constructed with a message string

---

## Rules to Implement

### Rule 1 — Borrow Limit
A member cannot borrow more than 5 books at once.
- Violation message must contain: `"borrow limit"`
- A request with exactly 5 books is fine. Only strictly more than 5 is a violation.

### Rule 2 — Reference Books
`REFERENCE` books are for in-library use only and cannot be borrowed.
- Violation message must contain: `"reference"`
- Flag this if any book in the request has genre `REFERENCE`.

### Rule 3 — Overdue Books
If any book in the request is marked as overdue, no new loans can be made.
- Violation message must contain: `"overdue"`
- Even one overdue book blocks the whole request.

### Rule 4 — Deposit Required
Books with a replacement cost **strictly over £30** require the deposit to be confirmed.
If `depositConfirmed` is `false` and any book costs more than £30, it is a violation.
- Violation message must contain: `"deposit"`
- Exactly £30 is not a violation. Only strictly above £30.

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
There are 12 tests. They all fail until your implementation is correct.
You are done when all 12 pass.

---

## Hint (read only if stuck)
<details>
<summary>Click to reveal</summary>

Structure your method like this:
1. Create an empty `List<Violation>` at the top.
2. Write one private method per rule. Each one adds to the list if the rule is broken.
3. Call all four methods.
4. At the end, if the list is empty return `approved()`, otherwise return `rejected(violations)`.

</details>
