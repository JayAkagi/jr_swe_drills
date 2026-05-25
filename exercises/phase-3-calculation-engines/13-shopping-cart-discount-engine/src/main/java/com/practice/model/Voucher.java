package com.practice.model;

import java.math.BigDecimal;

public class Voucher {
    private final String code;
    private final VoucherType type;
    private final BigDecimal value;

    public Voucher(String code, VoucherType type, BigDecimal value) {
        this.code = code;
        this.type = type;
        this.value = value;
    }

    public String getCode() { return code; }
    public VoucherType getType() { return type; }
    public BigDecimal getValue() { return value; }
}
