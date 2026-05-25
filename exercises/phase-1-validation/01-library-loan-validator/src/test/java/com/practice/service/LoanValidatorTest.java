package com.practice.service;

import com.practice.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoanValidatorTest {

    private LoanValidator validator;

    @BeforeEach
    void setUp() {
        validator = new LoanValidator();
    }

    // --- Rule 1: Borrow limit ---

    @Test
    void shouldApprove_whenExactlyFiveBooksRequested() {
        List<Book> books = List.of(
                book("Book A", BookGenre.FICTION, "10.00", false),
                book("Book B", BookGenre.FICTION, "10.00", false),
                book("Book C", BookGenre.FICTION, "10.00", false),
                book("Book D", BookGenre.FICTION, "10.00", false),
                book("Book E", BookGenre.FICTION, "10.00", false)
        );
        LoanValidationResult result = validator.validate(new LoanRequest("Alice", books, false));
        assertTrue(result.isApproved());
    }

    @Test
    void shouldReject_whenMoreThanFiveBooksRequested() {
        List<Book> books = List.of(
                book("Book A", BookGenre.FICTION, "10.00", false),
                book("Book B", BookGenre.FICTION, "10.00", false),
                book("Book C", BookGenre.FICTION, "10.00", false),
                book("Book D", BookGenre.FICTION, "10.00", false),
                book("Book E", BookGenre.FICTION, "10.00", false),
                book("Book F", BookGenre.FICTION, "10.00", false)
        );
        LoanValidationResult result = validator.validate(new LoanRequest("Alice", books, false));
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "borrow limit"));
    }

    // --- Rule 2: Reference books ---

    @Test
    void shouldReject_whenRequestContainsReferenceBook() {
        List<Book> books = List.of(
                book("Normal Book", BookGenre.FICTION, "10.00", false),
                book("Encyclopedia", BookGenre.REFERENCE, "50.00", false)
        );
        LoanValidationResult result = validator.validate(new LoanRequest("Alice", books, false));
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "reference"));
    }

    @Test
    void shouldApprove_whenNoReferenceBooks() {
        List<Book> books = List.of(
                book("Novel", BookGenre.FICTION, "10.00", false),
                book("History", BookGenre.NON_FICTION, "15.00", false)
        );
        LoanValidationResult result = validator.validate(new LoanRequest("Alice", books, false));
        assertTrue(result.isApproved());
    }

    // --- Rule 3: Overdue books ---

    @Test
    void shouldReject_whenMemberHasOverdueBook() {
        List<Book> books = List.of(
                book("New Book", BookGenre.FICTION, "10.00", false),
                book("Overdue Book", BookGenre.NON_FICTION, "10.00", true)
        );
        LoanValidationResult result = validator.validate(new LoanRequest("Alice", books, false));
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "overdue"));
    }

    @Test
    void shouldApprove_whenNoOverdueBooks() {
        List<Book> books = List.of(
                book("Clean Book", BookGenre.FICTION, "10.00", false)
        );
        LoanValidationResult result = validator.validate(new LoanRequest("Alice", books, false));
        assertTrue(result.isApproved());
    }

    // --- Rule 4: Deposit required ---

    @Test
    void shouldReject_whenExpensiveBookWithoutDepositConfirmation() {
        List<Book> books = List.of(
                book("Rare Book", BookGenre.NON_FICTION, "35.00", false)
        );
        LoanValidationResult result = validator.validate(new LoanRequest("Alice", books, false));
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "deposit"));
    }

    @Test
    void shouldApprove_whenExpensiveBookWithDepositConfirmed() {
        List<Book> books = List.of(
                book("Rare Book", BookGenre.NON_FICTION, "35.00", false)
        );
        LoanValidationResult result = validator.validate(new LoanRequest("Alice", books, true));
        assertTrue(result.isApproved());
    }

    @Test
    void shouldApprove_whenBookCostExactlyThirtyWithoutDeposit() {
        List<Book> books = List.of(
                book("Borderline Book", BookGenre.FICTION, "30.00", false)
        );
        LoanValidationResult result = validator.validate(new LoanRequest("Alice", books, false));
        assertTrue(result.isApproved());
    }

    // --- Multiple violations ---

    @Test
    void shouldCollectAllViolations_whenMultipleRulesBreached() {
        List<Book> books = List.of(
                book("Encyclopedia", BookGenre.REFERENCE, "10.00", false),
                book("Overdue Book", BookGenre.FICTION, "10.00", true),
                book("Extra 1", BookGenre.FICTION, "10.00", false),
                book("Extra 2", BookGenre.FICTION, "10.00", false),
                book("Extra 3", BookGenre.FICTION, "10.00", false),
                book("Extra 4", BookGenre.FICTION, "10.00", false)
        );
        LoanValidationResult result = validator.validate(new LoanRequest("Alice", books, false));
        assertFalse(result.isApproved());
        assertTrue(hasViolationContaining(result, "borrow limit"));
        assertTrue(hasViolationContaining(result, "reference"));
        assertTrue(hasViolationContaining(result, "overdue"));
    }

    // --- Happy path ---

    @Test
    void shouldApprove_whenAllRulesPass() {
        List<Book> books = List.of(
                book("Novel", BookGenre.FICTION, "10.00", false),
                book("History", BookGenre.NON_FICTION, "15.00", false)
        );
        LoanValidationResult result = validator.validate(new LoanRequest("Alice", books, false));
        assertTrue(result.isApproved());
        assertTrue(result.getViolations().isEmpty());
    }

    // --- Helpers ---

    private Book book(String title, BookGenre genre, String cost, boolean overdue) {
        return new Book(title, genre, new BigDecimal(cost), overdue);
    }

    private boolean hasViolationContaining(LoanValidationResult result, String keyword) {
        return result.getViolations().stream()
                .anyMatch(v -> v.getMessage().toLowerCase().contains(keyword.toLowerCase()));
    }
}
