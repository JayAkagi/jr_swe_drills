package com.practice.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.practice.model.Book;
import com.practice.model.LoanRequest;
import com.practice.model.LoanValidationResult;
import com.practice.model.Violation;
import com.practice.model.BookGenre;

public class LoanValidator {

    private BookGenre restrictBorrow = BookGenre.REFERENCE;
    private int borrowBookLimit = 5;    

    public LoanValidationResult validate(LoanRequest requests) {        
        List<Book> books = requests.getRequestedBooks();
        List<Violation> violations = new ArrayList<>(); 

        borrowLimit(violations, books);
        referenceBooks(violations, books);
        overdueBooks(violations, books);
        depositRequired(violations, books, requests);

        return hasViolationContaining(violations);
    }

    private LoanValidationResult hasViolationContaining(List<Violation> violations){
        return (violations.isEmpty()) ? LoanValidationResult.approved() : LoanValidationResult.rejected(violations);
    }

    private void borrowLimit(List<Violation> violations, List<Book> books){
        for (int i = 0; i < books.size(); i++) {
            if (i > borrowBookLimit - 1) {
                violations.add(new Violation(
                    "Borrow limit exceeded. cannot request more than " +
                borrowBookLimit + "."));
                break;
            }
        }
    }

    private void referenceBooks(List<Violation> violations, List<Book> books){
        for (Book book : books) {
            if (book.getGenre() == restrictBorrow) {
                violations.add(new Violation(
                    "Cannot borrow books that has a tag of: " +
                    restrictBorrow + "."));
            }
        }
    }

    private void overdueBooks(List<Violation> violations, List<Book> books){
        for (Book book : books) {
            if (book.isOverdue()) {
                violations.add(new Violation(
                    "Book: " + book + " is overdue."));
            }
        }
    }

    private void depositRequired(List<Violation> violations, List<Book> books, LoanRequest request){
        for (Book book : books) {
            if (book.getReplacementCostGBP().compareTo(new BigDecimal("30")) > 0) {
                if (!request.isDepositConfirmed()) {
                    violations.add(new Violation(
                        "deposit required."));
                    break;
                }
            }
        }
    }
}
