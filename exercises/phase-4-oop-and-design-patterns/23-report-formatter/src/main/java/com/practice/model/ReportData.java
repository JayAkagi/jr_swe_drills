package com.practice.model;

import java.time.LocalDateTime;
import java.util.List;

public class ReportData {

    private final String title;
    private final LocalDateTime generatedAt;
    private final List<ReportRow> rows;

    public ReportData(String title, LocalDateTime generatedAt, List<ReportRow> rows) {
        this.title = title;
        this.generatedAt = generatedAt;
        this.rows = rows;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public List<ReportRow> getRows() {
        return rows;
    }
}
