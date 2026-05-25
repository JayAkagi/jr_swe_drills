package com.practice.service;

import com.practice.model.InventoryDiff;
import com.practice.model.InventoryItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryDiffGeneratorTest {

    private InventoryDiffGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new InventoryDiffGenerator();
    }

    private InventoryItem item(String sku, String name, int stock, String price) {
        return new InventoryItem(sku, name, stock, new BigDecimal(price));
    }

    @Test
    void shouldReturnEmptyDiff_whenBothListsEmpty() {
        InventoryDiff diff = generator.diff(List.of(), List.of());
        assertTrue(diff.getAdded().isEmpty());
        assertTrue(diff.getRemoved().isEmpty());
        assertTrue(diff.getChanged().isEmpty());
    }

    @Test
    void shouldAddItem_whenOnlyInCurrentList() {
        InventoryItem newItem = item("SKU-001", "Widget", 10, "5.00");
        InventoryDiff diff = generator.diff(List.of(), List.of(newItem));
        assertEquals(1, diff.getAdded().size());
        assertEquals("SKU-001", diff.getAdded().get(0).getSku());
        assertTrue(diff.getRemoved().isEmpty());
        assertTrue(diff.getChanged().isEmpty());
    }

    @Test
    void shouldRemoveItem_whenOnlyInPreviousList() {
        InventoryItem oldItem = item("SKU-001", "Widget", 10, "5.00");
        InventoryDiff diff = generator.diff(List.of(oldItem), List.of());
        assertEquals(1, diff.getRemoved().size());
        assertEquals("SKU-001", diff.getRemoved().get(0).getSku());
        assertTrue(diff.getAdded().isEmpty());
        assertTrue(diff.getChanged().isEmpty());
    }

    @Test
    void shouldNotIncludeItem_whenIdenticalInBothLists() {
        InventoryItem item = item("SKU-001", "Widget", 10, "5.00");
        InventoryDiff diff = generator.diff(List.of(item), List.of(item("SKU-001", "Widget", 10, "5.00")));
        assertTrue(diff.getAdded().isEmpty());
        assertTrue(diff.getRemoved().isEmpty());
        assertTrue(diff.getChanged().isEmpty());
    }

    @Test
    void shouldChangeItem_whenStockLevelDiffers() {
        InventoryItem prev = item("SKU-001", "Widget", 10, "5.00");
        InventoryItem curr = item("SKU-001", "Widget", 15, "5.00");
        InventoryDiff diff = generator.diff(List.of(prev), List.of(curr));
        assertEquals(1, diff.getChanged().size());
        assertEquals("SKU-001", diff.getChanged().get(0).getSku());
    }

    @Test
    void shouldComputeStockDelta_whenStockChanged() {
        InventoryItem prev = item("SKU-001", "Widget", 10, "5.00");
        InventoryItem curr = item("SKU-001", "Widget", 15, "5.00");
        InventoryDiff diff = generator.diff(List.of(prev), List.of(curr));
        assertEquals(5, diff.getChanged().get(0).getStockDelta());
    }

    @Test
    void shouldHaveNegativeStockDelta_whenStockDecreased() {
        InventoryItem prev = item("SKU-001", "Widget", 20, "5.00");
        InventoryItem curr = item("SKU-001", "Widget", 8, "5.00");
        InventoryDiff diff = generator.diff(List.of(prev), List.of(curr));
        assertEquals(-12, diff.getChanged().get(0).getStockDelta());
    }

    @Test
    void shouldSetPriceChangedTrue_whenPriceDiffers() {
        InventoryItem prev = item("SKU-001", "Widget", 10, "5.00");
        InventoryItem curr = item("SKU-001", "Widget", 10, "6.00");
        InventoryDiff diff = generator.diff(List.of(prev), List.of(curr));
        assertEquals(1, diff.getChanged().size());
        assertTrue(diff.getChanged().get(0).isPriceChanged());
    }

    @Test
    void shouldSetPriceChangedFalse_whenOnlyStockChanges() {
        InventoryItem prev = item("SKU-001", "Widget", 10, "5.00");
        InventoryItem curr = item("SKU-001", "Widget", 20, "5.00");
        InventoryDiff diff = generator.diff(List.of(prev), List.of(curr));
        assertFalse(diff.getChanged().get(0).isPriceChanged());
    }

    @Test
    void shouldChangedItem_whenBothStockAndPriceChange() {
        InventoryItem prev = item("SKU-001", "Widget", 10, "5.00");
        InventoryItem curr = item("SKU-001", "Widget", 5, "7.50");
        InventoryDiff diff = generator.diff(List.of(prev), List.of(curr));
        assertEquals(1, diff.getChanged().size());
        var change = diff.getChanged().get(0);
        assertEquals(10, change.getPreviousStock());
        assertEquals(5, change.getNewStock());
        assertEquals(-5, change.getStockDelta());
        assertTrue(change.isPriceChanged());
        assertEquals(0, new BigDecimal("5.00").compareTo(change.getPreviousPrice()));
        assertEquals(0, new BigDecimal("7.50").compareTo(change.getNewPrice()));
    }

    @Test
    void shouldHandleMultipleItemsMixed() {
        List<InventoryItem> prev = List.of(
            item("SKU-001", "Widget", 10, "5.00"),
            item("SKU-002", "Gadget", 5, "20.00"),
            item("SKU-003", "OldItem", 3, "1.00")
        );
        List<InventoryItem> curr = List.of(
            item("SKU-001", "Widget", 10, "5.00"),
            item("SKU-002", "Gadget", 8, "20.00"),
            item("SKU-004", "NewItem", 12, "15.00")
        );
        InventoryDiff diff = generator.diff(prev, curr);
        assertEquals(1, diff.getAdded().size());
        assertEquals("SKU-004", diff.getAdded().get(0).getSku());
        assertEquals(1, diff.getRemoved().size());
        assertEquals("SKU-003", diff.getRemoved().get(0).getSku());
        assertEquals(1, diff.getChanged().size());
        assertEquals("SKU-002", diff.getChanged().get(0).getSku());
    }

    @Test
    void shouldUseSkuAsUniqueIdentifier_notProductName() {
        InventoryItem prev = item("SKU-001", "OldName", 10, "5.00");
        InventoryItem curr = item("SKU-001", "NewName", 10, "5.00");
        InventoryDiff diff = generator.diff(List.of(prev), List.of(curr));
        assertTrue(diff.getAdded().isEmpty());
        assertTrue(diff.getRemoved().isEmpty());
    }
}
