package com.practice.service;

import com.practice.model.ReportData;
import com.practice.model.ReportRow;

public abstract class ReportFormatter {

    public final String format(ReportData data) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    protected abstract String renderHeader(ReportData data);

    protected abstract String renderRow(ReportRow row);

    protected abstract String renderFooter(ReportData data);

    protected abstract String renderTotal(ReportData data);
}
