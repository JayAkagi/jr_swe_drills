# Exercise 30 — Dependency Resolver

## Scenario

You are building a task execution planner. Given a list of tasks with dependency relationships, produce an ordered execution plan such that every task runs only after all its dependencies have completed. Handle circular dependencies gracefully.

## Models

| Class | Fields |
|---|---|
| `Task` | taskId (String), name (String), dependsOn (List\<String\>) — taskIds this task depends on |
| `ExecutionPlan` | orderedTaskIds (List\<String\>), warnings (List\<String\>) |

## Your Task

Implement `DependencyResolver` in `src/main/java/com/practice/service/DependencyResolver.java`:

```java
public ExecutionPlan resolve(List<Task> tasks)
```

## Acceptance Criteria

1. A task appears in the output only after all its dependencies.
2. Tasks with no dependencies come first.
3. Circular dependencies: add a warning containing `"circular dependency"` and exclude cyclic tasks from the plan.
4. Stable ordering: tasks with no dependency relationship between them are sorted alphabetically by taskId.
5. The `orderedTaskIds` list is a valid topological order.

## Run Tests

```bash
mvn test
```

Expected: **12 tests passing**.

## Hint

Use Kahn's algorithm (BFS-based topological sort): build an in-degree map, initialise a queue with all zero-in-degree nodes sorted alphabetically, then process nodes one at a time. Any node never reached has a cycle. Use `TreeSet` or sorted collection for the queue to maintain alphabetical stable ordering.
