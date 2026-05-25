# Exercise 23 — Report Formatter (Template Method Pattern)

## Your Task

Implement the following classes in `src/main/java/com/practice/service/`:

- `ReportFormatter` — abstract base class defining the template method
- `PlainTextFormatter` — plain text output with dash separators
- `CsvFormatter` — comma-separated output with quoted strings
- `MarkdownFormatter` — pipe-delimited markdown table output

## Scenario

A reporting system needs to output the same financial data in multiple formats (plain text for CLI, CSV for spreadsheet import, Markdown for documentation). The structure of each report is always the same (header, rows, footer, total), but the rendering differs per format.

## Models

| Class | Fields |
|-------|--------|
| `ReportRow` | label (String), valueGBP (BigDecimal) |
| `ReportData` | title (String), generatedAt (LocalDateTime), rows (List\<ReportRow\>) |

## Classes to Implement

### `ReportFormatter` (abstract)

- `public final String format(ReportData data)` — the template method. Calls:
  1. `renderHeader(data)`
  2. `renderRow(row)` for each row in `data.getRows()`
  3. `renderFooter(data)`
  4. `renderTotal(data)`
  Concatenates all results and returns the combined string.
- `protected abstract String renderHeader(ReportData data)`
- `protected abstract String renderRow(ReportRow row)`
- `protected abstract String renderFooter(ReportData data)`
- `protected abstract String renderTotal(ReportData data)`

Note: `format()` is `final` — subclasses cannot override the overall structure.

### `PlainTextFormatter extends ReportFormatter`
Plain text with dashes as separators. Example:

```
Test Report
------------------
Revenue         1500.00
Expenses         750.50
Profit           749.50
------------------
Total:          3000.00
```

### `CsvFormatter extends ReportFormatter`
Comma-separated, with strings quoted. Example:

```
"title","Test Report"
"label","value"
"Revenue","1500.00"
"Expenses","750.50"
"Profit","749.50"
"Total","3000.00"
```

### `MarkdownFormatter extends ReportFormatter`
Pipe-separated markdown table. Example:

```
# Test Report
| Label | Value |
|-------|-------|
| Revenue | 1500.00 |
| Expenses | 750.50 |
| Profit | 749.50 |
| **Total** | 3000.00 |
```

## Criteria

1. `format()` is `final` — it cannot be overridden by subclasses
2. Each formatter produces output in its correct format
3. All sections (header, rows, footer, total) appear in the output
4. Adding a new format requires only a new subclass — no changes to `ReportFormatter`

## Test Count

Run `mvn test` — expect **12 tests** to pass after full implementation.

<details>
<summary>Hint</summary>

Implement `format()` in `ReportFormatter` using a `StringBuilder`:

```java
public final String format(ReportData data) {
    StringBuilder sb = new StringBuilder();
    sb.append(renderHeader(data));
    for (ReportRow row : data.getRows()) {
        sb.append(renderRow(row));
    }
    sb.append(renderFooter(data));
    sb.append(renderTotal(data));
    return sb.toString();
}
```

For the total value, sum all rows using `BigDecimal`:
```java
BigDecimal total = data.getRows().stream()
    .map(ReportRow::getValueGBP)
    .reduce(BigDecimal.ZERO, BigDecimal::add);
```

</details>
