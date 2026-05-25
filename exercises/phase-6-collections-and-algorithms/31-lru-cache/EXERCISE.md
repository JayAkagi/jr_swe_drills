# Exercise 31 — LRU Cache

## Scenario

You are implementing a fixed-capacity cache that evicts the Least Recently Used entry when full. This is a classic data structures problem with real-world applications in web servers, database query caches, and OS page replacement.

## Your Task

Implement `LRUCache<K, V>` in `src/main/java/com/practice/service/LRUCache.java`:

```java
public class LRUCache<K, V> {
    public LRUCache(int capacity) { ... }
    public V get(K key) { ... }
    public void put(K key, V value) { ... }
    public int size() { ... }
    public boolean containsKey(K key) { ... }
    public List<K> getKeysInEvictionOrder() { ... }
}
```

## Acceptance Criteria

1. `get()` on a missing key returns `null`.
2. `put()` on an existing key updates the value and promotes it to most-recently-used.
3. When at capacity, evict the least recently used entry before inserting a new one.
4. `get()` and `put()` both run in O(1) time.
5. `getKeysInEvictionOrder()` returns keys with the least recently used first.

## Run Tests

```bash
mvn test
```

Expected: **14 tests passing**.

## Hint

Extend `LinkedHashMap` with `accessOrder = true` (third constructor parameter). Override `removeEldestEntry` to return `true` when `size() > capacity`. The iteration order of a `LinkedHashMap` with access-order enabled goes from least recently used to most recently used — perfect for `getKeysInEvictionOrder()`.
