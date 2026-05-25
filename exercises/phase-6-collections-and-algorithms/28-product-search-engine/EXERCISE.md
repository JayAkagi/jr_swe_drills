# Exercise 28 — Product Search & Filter Engine

## Scenario

You are building the core search layer for an e-commerce platform. Users can filter a product catalogue by category, price range, rating, stock availability, and tags, then sort and paginate the results.

## Models

| Class | Fields |
|---|---|
| `SortField` | enum: PRICE, RATING, NAME |
| `SortOrder` | enum: ASC, DESC |
| `Product` | productId, name, category, priceGBP (BigDecimal), rating (double), inStock (boolean), tags (List\<String\>) |
| `SearchFilter` | category (nullable), minPrice (nullable), maxPrice (nullable), minRating (nullable), inStockOnly (boolean), tags (nullable/empty = no tag filter) |
| `SearchRequest` | filter, sortField, sortOrder, pageNumber (1-indexed), pageSize |
| `SearchResult` | items (List\<Product\>), totalMatching, pageNumber, totalPages |

## Your Task

Implement `ProductSearchEngine` in `src/main/java/com/practice/service/ProductSearchEngine.java`:

```java
public SearchResult search(List<Product> catalogue, SearchRequest request)
```

## Acceptance Criteria

1. All filter fields are nullable — null means no filter on that field.
2. Tag filter: the product must have ALL specified tags.
3. Results are sorted by the specified field and order.
4. Pagination: pageSize items per page; pageNumber is 1-indexed; `totalPages = ceil(totalMatching / pageSize)`.
5. Empty results return an empty list, not an error.

## Run Tests

```bash
mvn test
```

Expected: **17 tests passing**.

## Hint

Stream the catalogue, apply each non-null filter with `.filter()`, collect the full matching list to get `totalMatching`, then sort, then apply pagination with `subList`. Use `Comparator.comparing(...)` and `.reversed()` for sort direction.
