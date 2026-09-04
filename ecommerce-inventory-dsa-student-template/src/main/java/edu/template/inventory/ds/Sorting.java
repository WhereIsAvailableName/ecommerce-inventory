
package edu.template.inventory.ds;

import java.util.Comparator;

public final class Sorting {
    // Sorts the specified array using the selection sort algorithm
    public static <T> void selectionSort(T[] a, Comparator<? super T> cmp) {
        if (a == null || a.length <= 1) return;

        int n = a.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (cmp.compare(a[j], a[minIndex]) < 0) {
                    minIndex = j;
                }
            }

            // Swap the found minimum element with the element at current position
            if (minIndex != i) {
                T temp = a[i];
                a[i] = a[minIndex];
                a[minIndex] = temp;
            }
        }
    }

    // Sorts the specified array using the merge sort algorithm
    public static <T> void mergeSort(T[] a, Comparator<? super T> cmp) {
        if (a == null || a.length <= 1) return;

        T[] temp = (T[]) new Object[a.length];
        mergeSort(a, temp, 0, a.length - 1, cmp);
    }

    private static <T> void mergeSort(T[] a, T[] temp, int left, int right, Comparator<? super T> cmp) {
        if (left < right) {
            int mid = left + (right - left) / 2;

            mergeSort(a, temp, left, mid, cmp);
            mergeSort(a, temp, mid + 1, right, cmp);

            merge(a, temp, left, mid, right, cmp);
        }
    }

    private static <T> void merge(T[] a, T[] temp, int left, int mid, int right, Comparator<? super T> cmp) {
        if (right + 1 - left >= 0) System.arraycopy(a, left, temp, left, right + 1 - left);

        int i = left;
        int j = mid + 1;
        int k = left;

        while (i <= mid && j <= right) {
            if (cmp.compare(temp[i], temp[j]) <= 0) {
                a[k++] = temp[i++];
            } else {
                a[k++] = temp[j++];
            }
        }

        while (i <= mid) {
            a[k++] = temp[i++];
        }
    }

    // Sorts the specified array using the quick sort algorithm
    public static <T> void quickSort(T[] a, Comparator<? super T> cmp) {
        if (a == null || a.length <= 1) return;
        quickSort(a, 0, a.length - 1, cmp);
    }

    private static <T> void quickSort(T[] a, int low, int high, Comparator<? super T> cmp) {
        if (low < high) {
            int pivotIndex = partition(a, low, high, cmp);

            quickSort(a, low, pivotIndex - 1, cmp);
            quickSort(a, pivotIndex + 1, high, cmp);
        }
    }

    private static <T> int partition(T[] a, int low, int high, Comparator<? super T> cmp) {
        T pivot = a[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (cmp.compare(a[j], pivot) <= 0) {
                i++;
                T temp = a[i];
                a[i] = a[j];
                a[j] = temp;
            }
        }

        T temp = a[i + 1];
        a[i + 1] = a[high];
        a[high] = temp;

        return i + 1;
    }

    /*
    public static void main(String[] args) {
        System.out.println("=== Sorting Algorithms Sanity Tests ===");

        Integer[] numbers = {64, 34, 25, 12, 22, 11, 90};
        String[] names = {"John", "Alice", "Bob", "Charlie", "David"};

        Integer[] selectionCopy = numbers.clone();
        System.out.print("Before selection sort: ");
        for (int num : selectionCopy) System.out.print(num + " ");
        System.out.println();

        selectionSort(selectionCopy, Comparator.naturalOrder());
        System.out.print("After selection sort: ");
        for (int num : selectionCopy) System.out.print(num + " ");
        System.out.println();

        Integer[] mergeCopy = numbers.clone();
        mergeSort(mergeCopy, Comparator.naturalOrder());
        System.out.print("After merge sort: ");
        for (int num : mergeCopy) System.out.print(num + " ");
        System.out.println();

        Integer[] quickCopy = numbers.clone();
        quickSort(quickCopy, Comparator.naturalOrder());
        System.out.print("After quick sort: ");
        for (int num : quickCopy) System.out.print(num + " ");
        System.out.println();

        String[] nameCopy = names.clone();
        quickSort(nameCopy, Comparator.naturalOrder());
        System.out.print("Sorted names: ");
        for (String name : nameCopy) System.out.print(name + " ");
        System.out.println();

        quickSort(nameCopy, Comparator.reverseOrder());
        System.out.print("Reverse sorted names: ");
        for (String name : nameCopy) System.out.print(name + " ");
        System.out.println();

        System.out.println("✓ All sorting tests passed!");
    }

     */
}
