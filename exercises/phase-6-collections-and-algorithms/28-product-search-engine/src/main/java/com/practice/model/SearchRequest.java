package com.practice.model;

public class SearchRequest {
    private final SearchFilter filter;
    private final SortField sortField;
    private final SortOrder sortOrder;
    private final int pageNumber;
    private final int pageSize;

    public SearchRequest(SearchFilter filter, SortField sortField, SortOrder sortOrder, int pageNumber, int pageSize) {
        this.filter = filter;
        this.sortField = sortField;
        this.sortOrder = sortOrder;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public SearchFilter getFilter() { return filter; }
    public SortField getSortField() { return sortField; }
    public SortOrder getSortOrder() { return sortOrder; }
    public int getPageNumber() { return pageNumber; }
    public int getPageSize() { return pageSize; }
}
