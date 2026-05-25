package com.practice.service;

import com.practice.model.ExecutionPlan;
import com.practice.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DependencyResolverTest {

    private DependencyResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new DependencyResolver();
    }

    @Test
    void shouldReturnSingleTask_whenNoDependencies() {
        List<Task> tasks = List.of(new Task("A", "Task A", List.of()));
        ExecutionPlan plan = resolver.resolve(tasks);
        assertEquals(List.of("A"), plan.getOrderedTaskIds());
        assertTrue(plan.getWarnings().isEmpty());
    }

    @Test
    void shouldPlaceDependencyBeforeDependent() {
        List<Task> tasks = List.of(
            new Task("B", "Task B", List.of("A")),
            new Task("A", "Task A", List.of())
        );
        ExecutionPlan plan = resolver.resolve(tasks);
        List<String> order = plan.getOrderedTaskIds();
        assertTrue(order.indexOf("A") < order.indexOf("B"));
    }

    @Test
    void shouldResolveThreeTaskChainInOrder() {
        List<Task> tasks = List.of(
            new Task("C", "Task C", List.of("B")),
            new Task("B", "Task B", List.of("A")),
            new Task("A", "Task A", List.of())
        );
        ExecutionPlan plan = resolver.resolve(tasks);
        List<String> order = plan.getOrderedTaskIds();
        assertTrue(order.indexOf("A") < order.indexOf("B"));
        assertTrue(order.indexOf("B") < order.indexOf("C"));
    }

    @Test
    void shouldSortIndependentTasksAlphabetically() {
        List<Task> tasks = List.of(
            new Task("C", "Task C", List.of()),
            new Task("A", "Task A", List.of()),
            new Task("B", "Task B", List.of())
        );
        ExecutionPlan plan = resolver.resolve(tasks);
        assertEquals(List.of("A", "B", "C"), plan.getOrderedTaskIds());
    }

    @Test
    void shouldDetectCircularDependency_andAddWarning() {
        List<Task> tasks = List.of(
            new Task("A", "Task A", List.of("B")),
            new Task("B", "Task B", List.of("A"))
        );
        ExecutionPlan plan = resolver.resolve(tasks);
        assertFalse(plan.getWarnings().isEmpty());
        assertTrue(plan.getWarnings().stream().anyMatch(w -> w.toLowerCase().contains("circular dependency")));
    }

    @Test
    void shouldExcludeCyclicTasks_fromPlan() {
        List<Task> tasks = List.of(
            new Task("A", "Task A", List.of("B")),
            new Task("B", "Task B", List.of("A")),
            new Task("C", "Task C", List.of())
        );
        ExecutionPlan plan = resolver.resolve(tasks);
        assertTrue(plan.getOrderedTaskIds().contains("C"));
        assertFalse(plan.getOrderedTaskIds().contains("A") && plan.getOrderedTaskIds().contains("B"));
    }

    @Test
    void shouldResolveDiamondDependency_withDLast() {
        List<Task> tasks = List.of(
            new Task("A", "Task A", List.of("B", "C")),
            new Task("B", "Task B", List.of("D")),
            new Task("C", "Task C", List.of("D")),
            new Task("D", "Task D", List.of())
        );
        ExecutionPlan plan = resolver.resolve(tasks);
        List<String> order = plan.getOrderedTaskIds();
        assertTrue(order.indexOf("D") < order.indexOf("B"));
        assertTrue(order.indexOf("D") < order.indexOf("C"));
        assertTrue(order.indexOf("B") < order.indexOf("A"));
        assertTrue(order.indexOf("C") < order.indexOf("A"));
    }

    @Test
    void shouldHandleNonExistentDependency_gracefully() {
        List<Task> tasks = List.of(
            new Task("A", "Task A", List.of("UNKNOWN"))
        );
        assertDoesNotThrow(() -> resolver.resolve(tasks));
    }

    @Test
    void shouldReturnEmptyPlan_whenNoTasksProvided() {
        ExecutionPlan plan = resolver.resolve(List.of());
        assertTrue(plan.getOrderedTaskIds().isEmpty());
        assertTrue(plan.getWarnings().isEmpty());
    }

    @Test
    void shouldSortAllIndependentTasksAlphabetically_withMoreTasks() {
        List<Task> tasks = List.of(
            new Task("E", "Task E", List.of()),
            new Task("B", "Task B", List.of()),
            new Task("D", "Task D", List.of()),
            new Task("A", "Task A", List.of()),
            new Task("C", "Task C", List.of())
        );
        ExecutionPlan plan = resolver.resolve(tasks);
        assertEquals(List.of("A", "B", "C", "D", "E"), plan.getOrderedTaskIds());
    }

    @Test
    void shouldContainCircularDependencyInWarningText() {
        List<Task> tasks = List.of(
            new Task("X", "Task X", List.of("Y")),
            new Task("Y", "Task Y", List.of("X"))
        );
        ExecutionPlan plan = resolver.resolve(tasks);
        assertTrue(plan.getWarnings().stream()
            .anyMatch(w -> w.toLowerCase().contains("circular dependency")));
    }

    @Test
    void shouldProduceValidTopologicalOrder_forLinearChain() {
        List<Task> tasks = List.of(
            new Task("D", "Task D", List.of("C")),
            new Task("C", "Task C", List.of("B")),
            new Task("B", "Task B", List.of("A")),
            new Task("A", "Task A", List.of())
        );
        ExecutionPlan plan = resolver.resolve(tasks);
        List<String> order = plan.getOrderedTaskIds();
        assertEquals(4, order.size());
        assertTrue(order.indexOf("A") < order.indexOf("B"));
        assertTrue(order.indexOf("B") < order.indexOf("C"));
        assertTrue(order.indexOf("C") < order.indexOf("D"));
    }
}
