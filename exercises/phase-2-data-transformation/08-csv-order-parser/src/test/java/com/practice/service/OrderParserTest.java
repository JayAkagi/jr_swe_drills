package com.practice.service;

import com.practice.model.ParseResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderParserTest {

    private OrderParser parser;

    @BeforeEach
    void setUp() {
        parser = new OrderParser();
    }

    @Test
    void shouldReturnEmpty_whenInputIsEmpty() {
        ParseResult result = parser.parse("");
        assertTrue(result.getSuccessful().isEmpty());
        assertTrue(result.getErrors().isEmpty());
    }

    @Test
    void shouldReturnEmpty_whenOnlyHeader() {
        ParseResult result = parser.parse("orderId,customerName,productCode,quantity,priceGBP,orderDate");
        assertTrue(result.getSuccessful().isEmpty());
        assertTrue(result.getErrors().isEmpty());
    }

    @Test
    void shouldParseOneValidRow_whenHeaderPlusOneDataRow() {
        String csv = "orderId,customerName,productCode,quantity,priceGBP,orderDate\n" +
                     "ORD001,John Smith,PROD-A,2,19.99,2024-03-15";
        ParseResult result = parser.parse(csv);
        assertEquals(1, result.getSuccessful().size());
        assertTrue(result.getErrors().isEmpty());
        var order = result.getSuccessful().get(0);
        assertEquals("ORD001", order.getOrderId());
        assertEquals("John Smith", order.getCustomerName());
        assertEquals("PROD-A", order.getProductCode());
        assertEquals(2, order.getQuantity());
        assertEquals(0, new java.math.BigDecimal("19.99").compareTo(order.getPriceGBP()));
        assertEquals(java.time.LocalDate.of(2024, 3, 15), order.getOrderDate());
    }

    @Test
    void shouldParseMultipleValidRows() {
        String csv = "orderId,customerName,productCode,quantity,priceGBP,orderDate\n" +
                     "ORD001,Alice,PROD-A,1,10.00,2024-01-01\n" +
                     "ORD002,Bob,PROD-B,3,5.50,2024-01-02";
        ParseResult result = parser.parse(csv);
        assertEquals(2, result.getSuccessful().size());
        assertTrue(result.getErrors().isEmpty());
    }

    @Test
    void shouldProduceError_whenWrongColumnCount() {
        String csv = "orderId,customerName,productCode,quantity,priceGBP,orderDate\n" +
                     "ORD001,Alice,PROD-A,1,10.00";
        ParseResult result = parser.parse(csv);
        assertTrue(result.getSuccessful().isEmpty());
        assertEquals(1, result.getErrors().size());
        assertTrue(result.getErrors().get(0).getReason().contains("invalid format"));
    }

    @Test
    void shouldAssignCorrectRowNumber_whenWrongColumnCount() {
        String csv = "orderId,customerName,productCode,quantity,priceGBP,orderDate\n" +
                     "ORD001,Alice,PROD-A,1,10.00";
        ParseResult result = parser.parse(csv);
        assertEquals(2, result.getErrors().get(0).getRowNumber());
    }

    @Test
    void shouldProduceError_whenQuantityIsNotNumeric() {
        String csv = "orderId,customerName,productCode,quantity,priceGBP,orderDate\n" +
                     "ORD001,Alice,PROD-A,abc,10.00,2024-01-01";
        ParseResult result = parser.parse(csv);
        assertEquals(1, result.getErrors().size());
        assertTrue(result.getErrors().get(0).getReason().contains("invalid number"));
    }

    @Test
    void shouldProduceError_whenPriceIsNotNumeric() {
        String csv = "orderId,customerName,productCode,quantity,priceGBP,orderDate\n" +
                     "ORD001,Alice,PROD-A,2,notanumber,2024-01-01";
        ParseResult result = parser.parse(csv);
        assertEquals(1, result.getErrors().size());
        assertTrue(result.getErrors().get(0).getReason().contains("invalid number"));
    }

    @Test
    void shouldProduceError_whenQuantityIsZero() {
        String csv = "orderId,customerName,productCode,quantity,priceGBP,orderDate\n" +
                     "ORD001,Alice,PROD-A,0,10.00,2024-01-01";
        ParseResult result = parser.parse(csv);
        assertEquals(1, result.getErrors().size());
        assertTrue(result.getErrors().get(0).getReason().contains("invalid quantity"));
    }

    @Test
    void shouldProduceError_whenQuantityIsNegative() {
        String csv = "orderId,customerName,productCode,quantity,priceGBP,orderDate\n" +
                     "ORD001,Alice,PROD-A,-5,10.00,2024-01-01";
        ParseResult result = parser.parse(csv);
        assertEquals(1, result.getErrors().size());
        assertTrue(result.getErrors().get(0).getReason().contains("invalid quantity"));
    }

    @Test
    void shouldProduceError_whenDateIsInvalid() {
        String csv = "orderId,customerName,productCode,quantity,priceGBP,orderDate\n" +
                     "ORD001,Alice,PROD-A,2,10.00,not-a-date";
        ParseResult result = parser.parse(csv);
        assertEquals(1, result.getErrors().size());
        assertTrue(result.getErrors().get(0).getReason().contains("invalid date"));
    }

    @Test
    void shouldStorRawLine_whenParseErrorOccurs() {
        String badLine = "ORD001,Alice,PROD-A,0,10.00,2024-01-01";
        String csv = "orderId,customerName,productCode,quantity,priceGBP,orderDate\n" + badLine;
        ParseResult result = parser.parse(csv);
        assertEquals(badLine, result.getErrors().get(0).getRawLine());
    }

    @Test
    void shouldSeparateValidAndInvalidRows_whenMixed() {
        String csv = "orderId,customerName,productCode,quantity,priceGBP,orderDate\n" +
                     "ORD001,Alice,PROD-A,2,10.00,2024-01-01\n" +
                     "ORD002,Bob,PROD-B,abc,5.00,2024-01-02\n" +
                     "ORD003,Carol,PROD-C,1,3.00,2024-01-03";
        ParseResult result = parser.parse(csv);
        assertEquals(2, result.getSuccessful().size());
        assertEquals(1, result.getErrors().size());
    }

    @Test
    void shouldAssignCorrectRowNumbers_whenMultipleErrorRows() {
        String csv = "orderId,customerName,productCode,quantity,priceGBP,orderDate\n" +
                     "ORD001,Alice,PROD-A,bad,10.00,2024-01-01\n" +
                     "ORD002,Bob,PROD-B,2,10.00,2024-01-02\n" +
                     "ORD003,Carol,PROD-C,-1,10.00,2024-01-03";
        ParseResult result = parser.parse(csv);
        assertEquals(2, result.getErrors().get(0).getRowNumber());
        assertEquals(4, result.getErrors().get(1).getRowNumber());
    }
}
