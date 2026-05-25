package com.practice.model;

import java.math.BigDecimal;

public class Book {

    private final String title;
    private final BookGenre genre;
    private final BigDecimal replacementCostGBP;
    private final boolean isOverdue;

    public Book(String title, BookGenre genre, BigDecimal replacementCostGBP, boolean isOverdue) {
        this.title = title;
        this.genre = genre;
        this.replacementCostGBP = replacementCostGBP;
        this.isOverdue = isOverdue;
    }

    public String getTitle() {
        return title;
    }

    public BookGenre getGenre() {
        return genre;
    }

    public BigDecimal getReplacementCostGBP() {
        return replacementCostGBP;
    }

    public boolean isOverdue() {
        return isOverdue;
    }
}
