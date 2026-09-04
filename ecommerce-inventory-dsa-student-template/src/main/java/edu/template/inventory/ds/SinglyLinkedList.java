
package edu.template.inventory.ds;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class SinglyLinkedList<T> implements Iterable<T> {
    public static final class Node<T> {
        public T item;
        public Node<T> next;
        Node(T item) { this.item = item; }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // Adds an element to the beginning of the list
    public void addFirst(T item) {
        Node<T> newNode = new Node<>(item);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }
        size++;
    }

    // Adds an element to the end of the list
    public void addLast(T item) {
        Node<T> newNode = new Node<>(item);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    // Removes and returns the first element from the list
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        T removedItem = head.item;
        head = head.next;
        size--;
        if (isEmpty()) {
            tail = null;
        }
        return removedItem;
    }

    // Returns the element at the specified position in the list
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.item;
    }

    // Replaces the element at the specified position with the specified element
    public T set(int index, T item) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        T oldItem = current.item;
        current.item = item;
        return oldItem;
    }

    // Removes and returns the element at the specified position
    public T removeAt(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        if (index == 0) {
            return removeFirst();
        }

        Node<T> prev = head;
        for (int i = 0; i < index - 1; i++) {
            prev = prev.next;
        }

        Node<T> toRemove = prev.next;
        T removedItem = toRemove.item;
        prev.next = toRemove.next;

        // Update tail if removing the last element
        if (index == size - 1) {
            tail = prev;
        }

        size--;
        return removedItem;
    }

    @Override
    // Returns an iterator over elements in this list
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = head;

            public boolean hasNext() {
                return current != null;
            }

            // Returns the next element in the iteration
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more elements in the list");
                }
                T item = current.item;
                current = current.next;
                return item;
            }

            public void forEachRemaining(Consumer<? super T> action) {
                while (current != null) {
                    action.accept(current.item);
                    current = current.next;
                }
            }
        };
    }

    // Sorts the list using merge sort algorithm based on the extracted key
    public <U extends Comparable<U>> void mergeSort(java.util.function.Function<T, U> keyExtractor) {
        if (size <= 1) return;

        head = mergeSortHelper(head, keyExtractor);

        updateTail();
    }

    private <U extends Comparable<U>> Node<T> mergeSortHelper(Node<T> start, java.util.function.Function<T, U> keyExtractor) {
        if (start == null || start.next == null) {
            return start;
        }

        // Split the list into two halves
        Node<T> mid = findMiddle(start);
        Node<T> rightHead = mid.next;
        mid.next = null;

        Node<T> left = mergeSortHelper(start, keyExtractor);
        Node<T> right = mergeSortHelper(rightHead, keyExtractor);

        // Merge the sorted halves
        return merge(left, right, keyExtractor);
    }

    // Finds the middle node of the list using slow and fast pointer technique
    private Node<T> findMiddle(Node<T> start) {
        if (start == null) return null;

        Node<T> slow = start;
        Node<T> fast = start.next;  // fast从start.next开始，让slow指向中点或中点前一个

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }

    // Merges two sorted linked lists into one sorted list
    private <U extends Comparable<U>> Node<T> merge(Node<T> left, Node<T> right, java.util.function.Function<T, U> keyExtractor) {
        Node<T> dummy = new Node<>(null);
        Node<T> current = dummy;

        while (left != null && right != null) {
            U leftKey = keyExtractor.apply(left.item);
            U rightKey = keyExtractor.apply(right.item);

            if (leftKey.compareTo(rightKey) <= 0) {
                current.next = left;
                left = left.next;
            } else {
                current.next = right;
                right = right.next;
            }
            current = current.next;
        }

        current.next = (left != null) ? left : right;

        return dummy.next;
    }

    private void updateTail() {
        if (head == null) {
            tail = null;
            return;
        }

        tail = head;
        while (tail.next != null) {
            tail = tail.next;
        }
    }

    /*
    public static void main(String[] args) {
        System.out.println("=== SinglyLinkedList Sanity Tests ===");

        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();

        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        System.out.println("After addLast: " + list.size() + " elements");

        list.addFirst(0);
        System.out.println("After addFirst: " + list.size() + " elements");

        System.out.print("List contents: ");
        for (Integer num : list) {
            System.out.print(num + " ");
        }
        System.out.println();

        Integer first = list.removeFirst();
        System.out.println("Removed first: " + first);

        Integer atIndex1 = list.removeAt(1);
        System.out.println("Removed at index 1: " + atIndex1);

        list.addLast(5);
        list.addLast(2);
        list.addLast(8);
        list.addLast(1);

        System.out.print("Before sort: ");
        for (Integer num : list) System.out.print(num + " ");
        System.out.println();

        list.mergeSort(x -> x);

        System.out.print("After sort: ");
        for (Integer num : list) System.out.print(num + " ");
        System.out.println();

        System.out.println("✓ All linked list tests passed!");
    }

     */
}
