package com.practice.service;

import com.practice.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductSearchEngineTest {

    private ProductSearchEngine engine;
    private List<Product> catalogue;

    @BeforeEach
    void setUp() {
        engine = new ProductSearchEngine();
        catalogue = List.of(
            new Product("p1", "Alpha Widget", "Electronics", new BigDecimal("10.00"), 4.5, true, List.of("sale", "new")),
            new Product("p2", "Beta Gadget", "Electronics", new BigDecimal("25.00"), 3.8, false, List.of("new")),
            new Product("p3", "Gamma Tool", "Tools", new BigDecimal("15.00"), 4.9, true, List.of("sale")),
            new Product("p4", "Delta Item", "Tools", new BigDecimal("50.00"), 2.5, true, List.of()),
            new Product("p5", "Epsilon Gizmo", "Electronics", new BigDecimal("8.00"), 4.0, true, List.of("sale", "featured"))
        );
    }

    private SearchRequest noFilter(int page, int size) {
        SearchFilter filter = new SearchFilter(null, null, null, null, false, null);
        return new SearchRequest(filter, SortField.NAME, SortOrder.ASC, page, size);
    }

    @Test
    void shouldReturnEmptyResult_whenCatalogueIsEmpty() {
        SearchFilter filter = new SearchFilter(null, null, null, null, false, null);
        SearchRequest request = new SearchRequest(filter, SortField.NAME, SortOrder.ASC, 1, 10);
        SearchResult result = engine.search(List.of(), request);
        assertTrue(result.getItems().isEmpty());
        assertEquals(0, result.getTotalMatching());
        assertEquals(0, result.getTotalPages());
    }

    @Test
    void shouldReturnAllProducts_whenNoFiltersApplied() {
        SearchResult result = engine.search(catalogue, noFilter(1, 10));
        assertEquals(5, result.getTotalMatching());
        assertEquals(5, result.getItems().size());
    }

    @Test
    void shouldFilterByCategory() {
        SearchFilter filter = new SearchFilter("Electronics", null, null, null, false, null);
        SearchRequest request = new SearchRequest(filter, SortField.NAME, SortOrder.ASC, 1, 10);
        SearchResult result = engine.search(catalogue, request);
        assertEquals(3, result.getTotalMatching());
        assertTrue(result.getItems().stream().allMatch(p -> p.getCategory().equals("Electronics")));
    }

    @Test
    void shouldFilterByMinPrice() {
        SearchFilter filter = new SearchFilter(null, new BigDecimal("20.00"), null, null, false, null);
        SearchRequest request = new SearchRequest(filter, SortField.NAME, SortOrder.ASC, 1, 10);
        SearchResult result = engine.search(catalogue, request);
        assertTrue(result.getItems().stream().allMatch(p -> p.getPriceGBP().compareTo(new BigDecimal("20.00")) >= 0));
        assertEquals(2, result.getTotalMatching());
    }

    @Test
    void shouldFilterByMaxPrice() {
        SearchFilter filter = new SearchFilter(null, null, new BigDecimal("15.00"), null, false, null);
        SearchRequest request = new SearchRequest(filter, SortField.NAME, SortOrder.ASC, 1, 10);
        SearchResult result = engine.search(catalogue, request);
        assertTrue(result.getItems().stream().allMatch(p -> p.getPriceGBP().compareTo(new BigDecimal("15.00")) <= 0));
        assertEquals(3, result.getTotalMatching());
    }

    @Test
    void shouldFilterByMinRating() {
        SearchFilter filter = new SearchFilter(null, null, null, 4.0, false, null);
        SearchRequest request = new SearchRequest(filter, SortField.NAME, SortOrder.ASC, 1, 10);
        SearchResult result = engine.search(catalogue, request);
        assertTrue(result.getItems().stream().allMatch(p -> p.getRating() >= 4.0));
        assertEquals(3, result.getTotalMatching());
    }

    @Test
    void shouldFilterInStockOnly() {
        SearchFilter filter = new SearchFilter(null, null, null, null, true, null);
        SearchRequest request = new SearchRequest(filter, SortField.NAME, SortOrder.ASC, 1, 10);
        SearchResult result = engine.search(catalogue, request);
        assertTrue(result.getItems().stream().allMatch(Product::isInStock));
        assertEquals(4, result.getTotalMatching());
    }

    @Test
    void shouldFilterByTags_requireAllSpecifiedTags() {
        SearchFilter filter = new SearchFilter(null, null, null, null, false, List.of("sale", "new"));
        SearchRequest request = new SearchRequest(filter, SortField.NAME, SortOrder.ASC, 1, 10);
        SearchResult result = engine.search(catalogue, request);
        assertEquals(1, result.getTotalMatching());
        assertEquals("p1", result.getItems().get(0).getProductId());
    }

    @Test
    void shouldApplyMultipleFiltersCombined() {
        SearchFilter filter = new SearchFilter("Electronics", new BigDecimal("5.00"), new BigDecimal("20.00"), 4.0, true, null);
        SearchRequest request = new SearchRequest(filter, SortField.NAME, SortOrder.ASC, 1, 10);
        SearchResult result = engine.search(catalogue, request);
        assertEquals(2, result.getTotalMatching());
        assertTrue(result.getItems().stream().allMatch(p -> p.getCategory().equals("Electronics") && p.isInStock() && p.getRating() >= 4.0));
    }

    @Test
    void shouldSortByPriceAscending() {
        SearchFilter filter = new SearchFilter(null, null, null, null, false, null);
        SearchRequest request = new SearchRequest(filter, SortField.PRICE, SortOrder.ASC, 1, 10);
        SearchResult result = engine.search(catalogue, request);
        List<Product> items = result.getItems();
        for (int i = 1; i < items.size(); i++) {
            assertTrue(items.get(i - 1).getPriceGBP().compareTo(items.get(i).getPriceGBP()) <= 0);
        }
    }

    @Test
    void shouldSortByPriceDescending() {
        SearchFilter filter = new SearchFilter(null, null, null, null, false, null);
        SearchRequest request = new SearchRequest(filter, SortField.PRICE, SortOrder.DESC, 1, 10);
        SearchResult result = engine.search(catalogue, request);
        List<Product> items = result.getItems();
        for (int i = 1; i < items.size(); i++) {
            assertTrue(items.get(i - 1).getPriceGBP().compareTo(items.get(i).getPriceGBP()) >= 0);
        }
    }

    @Test
    void shouldSortByNameAscending() {
        SearchFilter filter = new SearchFilter(null, null, null, null, false, null);
        SearchRequest request = new SearchRequest(filter, SortField.NAME, SortOrder.ASC, 1, 10);
        SearchResult result = engine.search(catalogue, request);
        List<Product> items = result.getItems();
        for (int i = 1; i < items.size(); i++) {
            assertTrue(items.get(i - 1).getName().compareTo(items.get(i).getName()) <= 0);
        }
    }

    @Test
    void shouldSortByRatingDescending() {
        SearchFilter filter = new SearchFilter(null, null, null, null, false, null);
        SearchRequest request = new SearchRequest(filter, SortField.RATING, SortOrder.DESC, 1, 10);
        SearchResult result = engine.search(catalogue, request);
        List<Product> items = result.getItems();
        for (int i = 1; i < items.size(); i++) {
            assertTrue(items.get(i - 1).getRating() >= items.get(i).getRating());
        }
    }

    @Test
    void shouldPaginateCorrectly_returnSecondPage() {
        SearchFilter filter = new SearchFilter(null, null, null, null, false, null);
        SearchRequest request = new SearchRequest(filter, SortField.NAME, SortOrder.ASC, 2, 2);
        SearchResult result = engine.search(catalogue, request);
        assertEquals(2, result.getItems().size());
        assertEquals(2, result.getPageNumber());
    }

    @Test
    void shouldReturnTotalMatchingBeforePagination() {
        SearchFilter filter = new SearchFilter(null, null, null, null, false, null);
        SearchRequest request = new SearchRequest(filter, SortField.NAME, SortOrder.ASC, 1, 2);
        SearchResult result = engine.search(catalogue, request);
        assertEquals(5, result.getTotalMatching());
        assertEquals(2, result.getItems().size());
    }

    @Test
    void shouldCalculateTotalPagesCorrectly() {
        SearchFilter filter = new SearchFilter(null, null, null, null, false, null);
        SearchRequest request = new SearchRequest(filter, SortField.NAME, SortOrder.ASC, 1, 2);
        SearchResult result = engine.search(catalogue, request);
        assertEquals(3, result.getTotalPages());
    }
}
