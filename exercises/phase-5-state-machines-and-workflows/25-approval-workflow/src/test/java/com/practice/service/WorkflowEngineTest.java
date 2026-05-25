package com.practice.service;

import com.practice.model.ApprovalAction;
import com.practice.model.ApprovalDecision;
import com.practice.model.ApprovalRequest;
import com.practice.model.ApprovalStage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class WorkflowEngineTest {

    private WorkflowEngine engine;

    @BeforeEach
    void setUp() {
        engine = new WorkflowEngine();
    }

    @Test
    void shouldMoveToApproved_whenManagerApprovesLowValue() {
        ApprovalRequest request = new ApprovalRequest("REQ-001", "alice", new BigDecimal("200.00"));
        engine.action(request, ApprovalDecision.APPROVE, "Bob Manager", "Looks fine");
        assertEquals(ApprovalStage.APPROVED, request.getCurrentStage());
    }

    @Test
    void shouldMoveToFinanceReview_whenManagerApprovesHighValue() {
        ApprovalRequest request = new ApprovalRequest("REQ-002", "alice", new BigDecimal("750.00"));
        engine.action(request, ApprovalDecision.APPROVE, "Bob Manager", "Needs finance check");
        assertEquals(ApprovalStage.FINANCE_REVIEW, request.getCurrentStage());
    }

    @Test
    void shouldMoveToRejected_whenManagerRejects() {
        ApprovalRequest request = new ApprovalRequest("REQ-003", "alice", new BigDecimal("300.00"));
        engine.action(request, ApprovalDecision.REJECT, "Bob Manager", "Not justified");
        assertEquals(ApprovalStage.REJECTED, request.getCurrentStage());
    }

    @Test
    void shouldMoveToApproved_whenFinanceApproves() {
        ApprovalRequest request = new ApprovalRequest("REQ-004", "alice", new BigDecimal("800.00"));
        engine.action(request, ApprovalDecision.APPROVE, "Bob Manager", "Forwarding");
        engine.action(request, ApprovalDecision.APPROVE, "Carol Finance", "Budget available");
        assertEquals(ApprovalStage.APPROVED, request.getCurrentStage());
    }

    @Test
    void shouldMoveToRejected_whenFinanceRejects() {
        ApprovalRequest request = new ApprovalRequest("REQ-005", "alice", new BigDecimal("900.00"));
        engine.action(request, ApprovalDecision.APPROVE, "Bob Manager", "Forwarding");
        engine.action(request, ApprovalDecision.REJECT, "Carol Finance", "Over budget");
        assertEquals(ApprovalStage.REJECTED, request.getCurrentStage());
    }

    @Test
    void shouldThrowIllegalStateException_whenActingOnApproved() {
        ApprovalRequest request = new ApprovalRequest("REQ-006", "alice", new BigDecimal("100.00"));
        engine.action(request, ApprovalDecision.APPROVE, "Bob Manager", "Approved");
        assertThrows(IllegalStateException.class,
                () -> engine.action(request, ApprovalDecision.APPROVE, "Carol Finance", "Already done"));
    }

    @Test
    void shouldThrowIllegalStateException_whenActingOnRejected() {
        ApprovalRequest request = new ApprovalRequest("REQ-007", "alice", new BigDecimal("100.00"));
        engine.action(request, ApprovalDecision.REJECT, "Bob Manager", "Rejected");
        assertThrows(IllegalStateException.class,
                () -> engine.action(request, ApprovalDecision.APPROVE, "Carol Finance", "Overriding"));
    }

    @Test
    void shouldRecordActionInHistory_afterEachAction() {
        ApprovalRequest request = new ApprovalRequest("REQ-008", "alice", new BigDecimal("600.00"));
        engine.action(request, ApprovalDecision.APPROVE, "Bob Manager", "Forwarding");
        engine.action(request, ApprovalDecision.APPROVE, "Carol Finance", "Approved");
        assertEquals(2, request.getHistory().size());
    }

    @Test
    void shouldStoreActorName_inHistory() {
        ApprovalRequest request = new ApprovalRequest("REQ-009", "alice", new BigDecimal("200.00"));
        engine.action(request, ApprovalDecision.APPROVE, "Bob Manager", "Looks good");
        ApprovalAction action = request.getHistory().get(0);
        assertEquals("Bob Manager", action.getActorName());
    }

    @Test
    void shouldStoreComment_inHistory() {
        ApprovalRequest request = new ApprovalRequest("REQ-010", "alice", new BigDecimal("200.00"));
        engine.action(request, ApprovalDecision.APPROVE, "Bob Manager", "Within policy");
        ApprovalAction action = request.getHistory().get(0);
        assertEquals("Within policy", action.getComment());
    }

    @Test
    void shouldMoveToApproved_whenTotalAmountExactly500() {
        ApprovalRequest request = new ApprovalRequest("REQ-011", "alice", new BigDecimal("500.00"));
        engine.action(request, ApprovalDecision.APPROVE, "Bob Manager", "Exactly 500");
        assertEquals(ApprovalStage.APPROVED, request.getCurrentStage());
    }

    @Test
    void shouldMoveToFinanceReview_whenTotalAmountIs500Point01() {
        ApprovalRequest request = new ApprovalRequest("REQ-012", "alice", new BigDecimal("500.01"));
        engine.action(request, ApprovalDecision.APPROVE, "Bob Manager", "Just over limit");
        assertEquals(ApprovalStage.FINANCE_REVIEW, request.getCurrentStage());
    }
}
