package com.practice.model;

import java.util.List;

public class SearchResult {
    private final List<Product> items;
    private final int totalMatching;
    private final int pageNumber;
    private final int totalPages;

    public SearchResult(List<Product> items, int totalMatching, int pageNumber, int totalPages) {
        this.items = items;
        this.totalMatching = totalMatching;
        this.pageNumber = pageNumber;
        this.totalPages = totalPages;
    }

    public List<Product> getItems() { return items; }
    public int getTotalMatching() { return totalMatching; }
    public int getPageNumber() { return pageNumber; }
    public int getTotalPages() { return totalPages; }
}
