
package edu.template.inventory.ds;

import java.util.function.BiConsumer;

public class HashTable<K,V> {
    public HashTable() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    private Entry<K, V>[] table;
    private int size;
    private final double loadFactor;

    private static class Entry<K, V> {
        final K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    @SuppressWarnings("unchecked")
    public HashTable(int initialCapacity, double loadFactor) {
        this.table = (Entry<K, V>[]) new Entry[initialCapacity];
        this.loadFactor = loadFactor;
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // Inserts or updates a key-value pair in the hash table
    // Time complexity: O(1) average case, O(n) worst case (all collisions)
    public V put(K key, V value) {
        if (needsRehashing()) {
            rehash();
        }

        int index = getIndex(key);

        // Check if key already exists in the bucket - O(1) average
        Entry<K, V> current = table[index];
        while (current != null) {
            if (current.key.equals(key)) {
                V oldValue = current.value;
                current.value = value;
                return oldValue;
            }
            current = current.next;
        }

        // Key doesn't exist, add new entry to the head of the bucket - O(1)
        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.next = table[index];
        table[index] = newEntry;
        size++;
        return null;
    }

    // Get the value associated with the specified key
    // Time complexity: O(1) average case, O(n) worst case (all collisions)
    public V get(K key) {
        int index = getIndex(key);
        Entry<K, V> current = table[index];

        // Search for the key in the bucket - O(1) average
        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }

        return null;
    }

    // Checks if the hash table contains the specified key
    // Time complexity: O(1) average case, O(n) worst case
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    // Removes the key-value pair associated with the specified key
    // Time complexity: O(1) average case, O(n) worst case
    public V remove(K key) {
        int index = getIndex(key);
        Entry<K, V> current = table[index];
        Entry<K, V> prev = null;

        while (current != null) {
            if (current.key.equals(key)) {
                if (prev == null) {
                    // Remove head node
                    table[index] = current.next;
                } else {
                    // Remove middle or tail node
                    prev.next = current.next;
                }
                size--;
                return current.value;
            }
            prev = current;
            current = current.next;
        }

        return null;
    }

    // Performs the given action for each key-value pair in the hash table
    // Time complexity: O(n + capacity)
    public void forEach(BiConsumer<K, V> consumer) {
        for (Entry<K, V> bucketHead : table) {
            Entry<K, V> current = bucketHead;
            while (current != null) {
                consumer.accept(current.key, current.value);
                current = current.next;
            }
        }
    }

    // Computes the bucket index for a given key
    // Time complexity: O(1)
    private int getIndex(K key) {
        int hashCode = key.hashCode();
        return (hashCode & 0x7FFFFFFF) % table.length;
    }

    private boolean needsRehashing() {
        return (double) size / table.length > loadFactor;
    }

    @SuppressWarnings("unchecked")
    // Resizes the hash table and rehashes all entries
    // Time complexity: O(n) amortized over all insertions
    private void rehash() {
        int newCapacity = table.length * 2;
        Entry<K, V>[] newTable = (Entry<K, V>[]) new Entry[newCapacity];

        for (Entry<K, V> bucketHead : table) {
            Entry<K, V> current = bucketHead;
            while (current != null) {
                Entry<K, V> next = current.next;

                int newIndex = (current.key.hashCode() & 0x7FFFFFFF) % newCapacity;

                current.next = newTable[newIndex];
                newTable[newIndex] = current;

                current = next;
            }
        }

        table = newTable;
    }

    public int getCapacity() {
        return table.length;
    }

    public double getCurrentLoadFactor() {
        return (double) size / table.length;
    }

    /*
    public static void main(String[] args) {
        System.out.println("=== HashTable Sanity Tests ===");

        HashTable<String, Integer> table = new HashTable<>();

        table.put("apple", 10);
        table.put("banana", 20);
        table.put("cherry", 30);

        System.out.println("Size: " + table.size());
        System.out.println("Get apple: " + table.get("apple"));
        System.out.println("Contains banana: " + table.containsKey("banana"));

        table.put("apple", 15);
        System.out.println("Updated apple: " + table.get("apple"));

        Integer removed = table.remove("banana");
        System.out.println("Removed banana: " + removed);
        System.out.println("Contains banana after removal: " + table.containsKey("banana"));

        System.out.println("Initial capacity: " + table.getCapacity());
        for (int i = 0; i < 20; i++) {
            table.put("key" + i, i);
        }
        System.out.println("Capacity after rehashing: " + table.getCapacity());
        System.out.println("Final size: " + table.size());
        System.out.println("Load factor: " + table.getCurrentLoadFactor());

        System.out.println("All entries:");
        table.forEach((k, v) -> System.out.println(k + " -> " + v));

        System.out.println("✓ All hash table tests passed!");
    }

     */
}
