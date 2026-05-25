package com.practice.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LRUCacheTest {

    @Test
    void shouldReturnNull_whenGetOnEmptyCache() {
        LRUCache<String, Integer> cache = new LRUCache<>(3);
        assertNull(cache.get("missing"));
    }

    @Test
    void shouldReturnValue_afterPut() {
        LRUCache<String, Integer> cache = new LRUCache<>(3);
        cache.put("a", 1);
        assertEquals(1, cache.get("a"));
    }

    @Test
    void shouldUpdateValue_whenPutExistingKey() {
        LRUCache<String, Integer> cache = new LRUCache<>(3);
        cache.put("a", 1);
        cache.put("a", 99);
        assertEquals(99, cache.get("a"));
    }

    @Test
    void shouldReportCorrectSize() {
        LRUCache<String, Integer> cache = new LRUCache<>(5);
        cache.put("a", 1);
        cache.put("b", 2);
        assertEquals(2, cache.size());
    }

    @Test
    void shouldReturnTrue_forContainsKeyAfterPut() {
        LRUCache<String, Integer> cache = new LRUCache<>(3);
        cache.put("x", 42);
        assertTrue(cache.containsKey("x"));
    }

    @Test
    void shouldEvictLRU_whenCapacityOneAndSecondPut() {
        LRUCache<String, Integer> cache = new LRUCache<>(1);
        cache.put("a", 1);
        cache.put("b", 2);
        assertNull(cache.get("a"));
        assertEquals(2, cache.get("b"));
    }

    @Test
    void shouldEvictLeastRecentlyUsed_whenCapacityTwo() {
        LRUCache<String, Integer> cache = new LRUCache<>(2);
        cache.put("a", 1);
        cache.put("b", 2);
        cache.put("c", 3);
        assertNull(cache.get("a"));
        assertEquals(2, cache.get("b"));
        assertEquals(3, cache.get("c"));
    }

    @Test
    void shouldPromoteToMRU_onGet() {
        LRUCache<String, Integer> cache = new LRUCache<>(2);
        cache.put("a", 1);
        cache.put("b", 2);
        cache.get("a");
        cache.put("c", 3);
        assertNull(cache.get("b"));
        assertEquals(1, cache.get("a"));
        assertEquals(3, cache.get("c"));
    }

    @Test
    void shouldPromoteToMRU_onPutExistingKey() {
        LRUCache<String, Integer> cache = new LRUCache<>(2);
        cache.put("a", 1);
        cache.put("b", 2);
        cache.put("a", 10);
        cache.put("c", 3);
        assertNull(cache.get("b"));
        assertEquals(10, cache.get("a"));
        assertEquals(3, cache.get("c"));
    }

    @Test
    void shouldReturnKeysInEvictionOrder_LRUFirst() {
        LRUCache<String, Integer> cache = new LRUCache<>(3);
        cache.put("a", 1);
        cache.put("b", 2);
        cache.put("c", 3);
        List<String> order = cache.getKeysInEvictionOrder();
        assertEquals("a", order.get(0));
        assertEquals("c", order.get(order.size() - 1));
    }

    @Test
    void shouldReflectGetAccess_inEvictionOrder() {
        LRUCache<String, Integer> cache = new LRUCache<>(3);
        cache.put("a", 1);
        cache.put("b", 2);
        cache.put("c", 3);
        cache.get("a");
        List<String> order = cache.getKeysInEvictionOrder();
        assertEquals("b", order.get(0));
        assertEquals("a", order.get(order.size() - 1));
    }

    @Test
    void shouldKeepSizeAtCapacity_afterEvictions() {
        LRUCache<String, Integer> cache = new LRUCache<>(2);
        cache.put("a", 1);
        cache.put("b", 2);
        cache.put("c", 3);
        cache.put("d", 4);
        assertEquals(2, cache.size());
    }

    @Test
    void shouldReturnFalseForContainsKey_afterEviction() {
        LRUCache<String, Integer> cache = new LRUCache<>(2);
        cache.put("a", 1);
        cache.put("b", 2);
        cache.put("c", 3);
        assertFalse(cache.containsKey("a"));
    }

    @Test
    void shouldReorderEvictionList_afterMultipleGets() {
        LRUCache<String, Integer> cache = new LRUCache<>(3);
        cache.put("a", 1);
        cache.put("b", 2);
        cache.put("c", 3);
        cache.get("a");
        cache.get("b");
        List<String> order = cache.getKeysInEvictionOrder();
        assertEquals("c", order.get(0));
        assertEquals("b", order.get(order.size() - 1));
    }
}
