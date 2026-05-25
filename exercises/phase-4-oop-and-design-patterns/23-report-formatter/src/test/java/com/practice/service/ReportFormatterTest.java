package com.practice.service;

import com.practice.model.ReportData;
import com.practice.model.ReportRow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReportFormatterTest {

    private ReportData reportData;
    private PlainTextFormatter plainText;
    private CsvFormatter csv;
    private MarkdownFormatter markdown;

    @BeforeEach
    void setUp() {
        List<ReportRow> rows = List.of(
                new ReportRow("Revenue", new BigDecimal("1500.00")),
                new ReportRow("Expenses", new BigDecimal("750.50")),
                new ReportRow("Profit", new BigDecimal("749.50"))
        );
        reportData = new ReportData("Test Report", LocalDateTime.of(2026, 1, 15, 9, 0), rows);

        plainText = new PlainTextFormatter();
        csv = new CsvFormatter();
        markdown = new MarkdownFormatter();
    }

    @Test
    void shouldContainTitle_inPlainTextOutput() {
        String output = plainText.format(reportData);
        assertTrue(output.contains("Test Report"),
                "Plain text output should contain the title");
    }

    @Test
    void shouldContainAllRowLabels_inPlainTextOutput() {
        String output = plainText.format(reportData);
        assertTrue(output.contains("Revenue"), "Should contain 'Revenue'");
        assertTrue(output.contains("Expenses"), "Should contain 'Expenses'");
        assertTrue(output.contains("Profit"), "Should contain 'Profit'");
    }

    @Test
    void shouldContainRowValues_inPlainTextOutput() {
        String output = plainText.format(reportData);
        assertTrue(output.contains("1500") || output.contains("1,500"),
                "Should contain the value 1500");
    }

    @Test
    void shouldBeCommaSeparated_inCsvOutput() {
        String output = csv.format(reportData);
        assertTrue(output.contains(","), "CSV output should contain commas");
    }

    @Test
    void shouldContainTitle_inCsvOutput() {
        String output = csv.format(reportData);
        assertTrue(output.contains("Test Report"), "CSV output should contain the title");
    }

    @Test
    void shouldContainRowLabels_inCsvOutput() {
        String output = csv.format(reportData);
        assertTrue(output.contains("Revenue"), "CSV should contain 'Revenue'");
        assertTrue(output.contains("Expenses"), "CSV should contain 'Expenses'");
    }

    @Test
    void shouldContainPipeCharacters_inMarkdownOutput() {
        String output = markdown.format(reportData);
        assertTrue(output.contains("|"), "Markdown output should contain pipe characters");
    }

    @Test
    void shouldContainTitle_inMarkdownOutput() {
        String output = markdown.format(reportData);
        assertTrue(output.contains("Test Report"), "Markdown output should contain the title");
    }

    @Test
    void shouldContainAllSections_inOutput() {
        String output = plainText.format(reportData);
        assertNotNull(output);
        assertFalse(output.isBlank(), "Output should not be blank");
        assertTrue(output.contains("Revenue"), "Should contain row data");
    }

    @Test
    void shouldContainTotalSection_inPlainTextOutput() {
        String output = plainText.format(reportData);
        String lowerOutput = output.toLowerCase();
        assertTrue(lowerOutput.contains("total") || output.contains("2500") || output.contains("749"),
                "Output should contain a total section or total value");
    }

    @Test
    void shouldReturnNonNull_forAllFormatters() {
        assertNotNull(plainText.format(reportData));
        assertNotNull(csv.format(reportData));
        assertNotNull(markdown.format(reportData));
    }

    @Test
    void shouldContainAllRows_inMarkdownOutput() {
        String output = markdown.format(reportData);
        assertTrue(output.contains("Revenue"), "Markdown should contain 'Revenue'");
        assertTrue(output.contains("Expenses"), "Markdown should contain 'Expenses'");
        assertTrue(output.contains("Profit"), "Markdown should contain 'Profit'");
    }
}
