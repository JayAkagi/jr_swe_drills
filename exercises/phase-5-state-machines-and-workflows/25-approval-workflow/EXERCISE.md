# Exercise 25 — Approval Workflow Engine

## Your Task

Implement `WorkflowEngine` in `src/main/java/com/practice/service/`.

## Scenario

A company processes expense requests through a tiered approval workflow. Requests below or equal to £500 only need manager approval. Requests above £500 must also pass finance review. Your engine drives these decisions based on the current stage and the actor's decision.

## Models

| Class | Fields | Notes |
|-------|--------|-------|
| `ApprovalStage` | SUBMITTED, MANAGER_REVIEW, FINANCE_REVIEW, APPROVED, REJECTED | Pre-built enum |
| `ApprovalDecision` | APPROVE, REJECT | Pre-built enum |
| `ApprovalAction` | stage, decision, actorName, comment, actionedAt | Pre-built, immutable |
| `ApprovalRequest` | requestId, submittedBy, totalAmountGBP, currentStage, history | Pre-built; constructor sets currentStage=MANAGER\_REVIEW |

## Routing Rules

| Current Stage | Decision | Total Amount | Next Stage |
|--------------|----------|--------------|------------|
| MANAGER_REVIEW | APPROVE | > £500 | FINANCE_REVIEW |
| MANAGER_REVIEW | APPROVE | <= £500 | APPROVED |
| MANAGER_REVIEW | REJECT | any | REJECTED |
| FINANCE_REVIEW | APPROVE | any | APPROVED |
| FINANCE_REVIEW | REJECT | any | REJECTED |
| APPROVED | any | any | throw IllegalStateException |
| REJECTED | any | any | throw IllegalStateException |

## Class Descriptions

### `WorkflowEngine`
Single public method: `ApprovalRequest action(ApprovalRequest request, ApprovalDecision decision, String actorName, String comment)`.
- Record the action (current stage, decision, actorName, comment, now) in `request.history`.
- Advance `request.currentStage` according to the routing rules.
- Return the same `request` object.
- Throw `IllegalStateException` if called on a terminal stage (APPROVED or REJECTED).

## Acceptance Criteria

1. Manager approving a request of £500 or less moves it to APPROVED.
2. Manager approving a request above £500 moves it to FINANCE_REVIEW.
3. Manager rejecting moves it to REJECTED.
4. Finance approving moves it to APPROVED.
5. Finance rejecting moves it to REJECTED.
6. Acting on APPROVED or REJECTED throws `IllegalStateException`.
7. Every action is recorded in history with the correct stage, decision, actorName, and comment.

## Test Count

Run `mvn test` — all 12 tests must pass.

## Hint

Check `request.getCurrentStage()` first, throw if terminal, then route using a switch expression on the stage and decision combination.
