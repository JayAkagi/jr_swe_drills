# Exercise 27 — Job Queue Processor

## Your Task

Implement `JobQueue` in `src/main/java/com/practice/service/`.

## Scenario

A background processing system manages jobs with different priorities. Higher-priority jobs must run first. When multiple jobs share the same priority, they are processed in the order they were enqueued (FIFO). Failed jobs that have not exceeded their retry limit are re-queued; those that have reached their limit are marked DEAD.

## Models

| Class | Fields | Notes |
|-------|--------|-------|
| `JobStatus` | QUEUED, RUNNING, COMPLETED, FAILED, DEAD | Pre-built enum |
| `Job` | jobId, type, priority, payload, status, attemptCount, maxAttempts, createdAt, lastAttemptAt, errorMessage | Pre-built; status=QUEUED, attemptCount=0 initially; lastAttemptAt and errorMessage start null |

## Class Descriptions

### `JobQueue`
- `void enqueue(Job job)` — Add the job to the queue maintaining priority order (highest priority first; FIFO within same priority). Sets status to QUEUED.
- `Job poll()` — Remove and return the next QUEUED job (highest priority; FIFO within equal priority); mark it RUNNING and update `lastAttemptAt` to now. Return `null` if the queue is empty.
- `void complete(String jobId, String output)` — Mark the job COMPLETED.
- `void fail(String jobId, String errorMessage)` — If `attemptCount < maxAttempts`: increment `attemptCount`, set `errorMessage`, reset status to QUEUED, and re-enqueue it. Otherwise: set status to DEAD and set `errorMessage`.
- `List<Job> getByStatus(JobStatus status)` — Return all tracked jobs (queued or not) that have the given status.

## Acceptance Criteria

1. Higher-priority jobs are polled before lower-priority jobs.
2. Jobs with equal priority are polled in FIFO order (insertion order).
3. Failed jobs below `maxAttempts` are re-queued with an incremented `attemptCount`.
4. Failed jobs at `maxAttempts` are marked DEAD and not re-queued.
5. `poll()` returns `null` when no QUEUED jobs remain.
6. `getByStatus` returns all jobs with the requested status.

## Test Count

Run `mvn test` — all 14 tests must pass.

## Hint

Maintain a `List<Job>` as the internal queue and a separate `Map<String, Job>` for all tracked jobs. For priority ordering, insert into the list maintaining sorted order or sort on `poll()`. For FIFO within same priority, preserve insertion order among equal-priority entries.
