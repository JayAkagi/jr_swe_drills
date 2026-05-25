package com.practice.model;

import java.util.List;

public class LoanRequest {

    private final String memberName;
    private final List<Book> requestedBooks;
    private final boolean depositConfirmed;

    public LoanRequest(String memberName, List<Book> requestedBooks, boolean depositConfirmed) {
        this.memberName = memberName;
        this.requestedBooks = requestedBooks;
        this.depositConfirmed = depositConfirmed;
    }

    public String getMemberName() {
        return memberName;
    }

    public List<Book> getRequestedBooks() {
        return requestedBooks;
    }

    public boolean isDepositConfirmed() {
        return depositConfirmed;
    }
}
