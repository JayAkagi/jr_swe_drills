package com.practice.model;

import java.util.List;

public class InventoryDiff {
    private final List<InventoryItem> added;
    private final List<InventoryItem> removed;
    private final List<StockChange> changed;

    public InventoryDiff(List<InventoryItem> added, List<InventoryItem> removed, List<StockChange> changed) {
        this.added = added;
        this.removed = removed;
        this.changed = changed;
    }

    public List<InventoryItem> getAdded() { return added; }
    public List<InventoryItem> getRemoved() { return removed; }
    public List<StockChange> getChanged() { return changed; }
}
