package com.practice.model;

import java.util.List;

public class ParseResult {
    private final List<Order> successful;
    private final List<ParseError> errors;

    public ParseResult(List<Order> successful, List<ParseError> errors) {
        this.successful = successful;
        this.errors = errors;
    }

    public List<Order> getSuccessful() { return successful; }
    public List<ParseError> getErrors() { return errors; }
}
