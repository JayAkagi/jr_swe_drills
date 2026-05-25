package com.practice.model;

public class ParseError {
    private final int rowNumber;
    private final String rawLine;
    private final String reason;

    public ParseError(int rowNumber, String rawLine, String reason) {
        this.rowNumber = rowNumber;
        this.rawLine = rawLine;
        this.reason = reason;
    }

    public int getRowNumber() { return rowNumber; }
    public String getRawLine() { return rawLine; }
    public String getReason() { return reason; }
}
