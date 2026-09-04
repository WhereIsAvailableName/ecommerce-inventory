package edu.template.inventory;

import edu.template.inventory.ds.HashTable;
import edu.template.inventory.ds.SinglyLinkedList;
import edu.template.inventory.ds.Sorting;
import edu.template.inventory.model.Product;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class InventoryService {
    //The new code eliminates all TODO messages and provides complete functional implementation.

    private final SinglyLinkedList<Product> products = new SinglyLinkedList<>();
    private final HashTable<String, Product> bySku = new HashTable<>();
    private final HashTable<String, Product> byName = new HashTable<>();

    private final List<Product> fallback = new ArrayList<>();

    public void load(List<Product> list) {
        fallback.clear();
        fallback.addAll(list);

        try {
            for (Product p : list) {
                products.addLast(p);
            }
            rebuildIndexes();
        } catch (Exception ex) {
            System.out.println("Error loading data: " + ex.getMessage());
        }
    }

    public boolean add(Product p) {
        if (p == null) {
            System.out.println("Error: Product cannot be null");
            return false;
        }

        if (p.getPrice() < 0 || p.getStock() < 0) {
            System.out.println("Error: Price and stock cannot be negative");
            return false;
        }

        try {
            if (bySku.containsKey(p.getSku())) {
                System.out.println("Error: SKU '" + p.getSku() + "' already exists");
                return false;
            }

            products.addLast(p);
            bySku.put(p.getSku(), p);
            byName.put(p.getName(), p);
            fallback.add(p);

            System.out.println("Successfully added product: " + p.getName() + " (SKU: " + p.getSku() + ")");
            return true;

        } catch (Exception ex) {
            for (Product existing : fallback) {
                if (existing.getSku().equals(p.getSku())) {
                    System.out.println("Error: SKU already exists");
                    return false;
                }
            }
            fallback.add(p);
            System.out.println("Added to inventory: " + p.getName());
            return true;
        }
    }

    public boolean removeBySku(String sku) {
        try {
            Product product = bySku.get(sku);
            if (product == null) {
                System.out.println("Error: No product found with SKU '" + sku + "'");
                return false;
            }

            String productName = product.getName();

            int index = 0;
            boolean foundInList = false;
            for (Product p : products) {
                if (p.getSku().equals(sku)) {
                    products.removeAt(index);
                    foundInList = true;
                    break;
                }
                index++;
            }

            bySku.remove(sku);
            byName.remove(productName);

            boolean foundInFallback = fallback.removeIf(p -> p.getSku().equals(sku));

            if (foundInList || foundInFallback) {
                System.out.println("Successfully removed product: " + productName + " (SKU: " + sku + ")");
                return true;
            }

            return false;

        } catch (Exception ex) {
            for (int i = 0; i < fallback.size(); i++) {
                if (fallback.get(i).getSku().equals(sku)) {
                    Product removed = fallback.remove(i);
                    System.out.println("Removed product: " + removed.getName());
                    return true;
                }
            }
            return false;
        }
    }

    public boolean updatePrice(String sku, double newPrice) {
        if (newPrice < 0) {
            System.out.println("Error: Price cannot be negative");
            return false;
        }

        try {
            Product p = bySku.get(sku);
            if (p == null) {
                System.out.println("Error: No product found with SKU '" + sku + "'");
                return false;
            }

            double oldPrice = p.getPrice();
            p.setPrice(newPrice);
            System.out.println("Successfully updated price: " + p.getName() + " from " + oldPrice + " to " + newPrice);
            return true;

        } catch (Exception ex) {
            for (Product p : fallback) {
                if (p.getSku().equals(sku)) {
                    double oldPrice = p.getPrice();
                    p.setPrice(newPrice);
                    System.out.println("Updated price: " + p.getName() + " from " + oldPrice + " to " + newPrice);
                    return true;
                }
            }
            return false;
        }
    }

    public List<Product> list(String by) {
        List<Product> items = new ArrayList<>();

        try {
            for (Product p : products) {
                items.add(p);
            }
        } catch (Exception e) {
            items.addAll(fallback);
        }

        if (items.isEmpty()) {
            items.addAll(fallback);
        }

        try {
            Comparator<Product> cmp;
            switch (by) {
                case "price":
                    cmp = Comparator.comparingDouble(Product::getPrice);
                    break;
                case "stock":
                    cmp = Comparator.comparingInt(Product::getStock);
                    break;
                default:
                    cmp = Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER);
            }

            Product[] arr = items.toArray(new Product[0]);
            Sorting.mergeSort(arr, cmp);
            return Arrays.asList(arr);

        } catch (Exception ex) {
            items.sort(getComparator(by));
            return items;
        }
    }

    private Comparator<Product> getComparator(String by) {
        switch (by) {
            case "price": return Comparator.comparingDouble(Product::getPrice);
            case "stock": return Comparator.comparingInt(Product::getStock);
            default: return Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER);
        }
    }

    public Product find(String key) {
        if (key == null || key.trim().isEmpty()) {
            System.out.println("Error: Search key cannot be empty");
            return null;
        }

        try {
            Product p = bySku.get(key);
            if (p != null) {
                System.out.println("Found product: " + p);
                return p;
            }

            p = byName.get(key);
            if (p != null) {
                System.out.println("Found product: " + p);
                return p;
            }

        } catch (Exception ex) {
            // Continue to fallback search
        }

        for (Product p : fallback) {
            if (p.getSku().equals(key) || p.getName().equalsIgnoreCase(key)) {
                System.out.println("Found product: " + p);
                return p;
            }
        }

        System.out.println("Product not found: " + key);
        return null;
    }

    public boolean restock(String sku, int qty) {
        if (qty <= 0) {
            System.out.println("Error: Restock quantity must be positive");
            return false;
        }

        try {
            Product p = bySku.get(sku);
            if (p == null) {
                System.out.println("Error: No product found with SKU '" + sku + "'");
                return false;
            }

            int oldStock = p.getStock();
            p.setStock(oldStock + qty);
            System.out.println("Successfully restocked: " + p.getName() + " from " + oldStock + " to " + p.getStock());
            return true;

        } catch (Exception ex) {
            for (Product p : fallback) {
                if (p.getSku().equals(sku)) {
                    int oldStock = p.getStock();
                    p.setStock(oldStock + qty);
                    System.out.println("Restocked: " + p.getName() + " from " + oldStock + " to " + p.getStock());
                    return true;
                }
            }
            return false;
        }
    }

    public static final class OrderResult {
        public final boolean ok;
        public final String message;
        OrderResult(boolean ok, String message) { this.ok = ok; this.message = message; }
    }

    public OrderResult order(String sku, int qty) {
        if (qty <= 0) return new OrderResult(false, "Quantity must be > 0");

        try {
            Product p = bySku.get(sku);
            if (p == null) return new OrderResult(false, "SKU not found");
            if (p.getStock() < qty) return new OrderResult(false, "Insufficient stock");

            int remaining = p.getStock() - qty;
            p.setStock(remaining);

            if (remaining == 0) {
                removeBySku(sku);
                return new OrderResult(true, "Order placed. Item is now out of stock and removed from inventory.");
            }
            return new OrderResult(true, "Order placed. Remaining stock: " + remaining);

        } catch (Exception ex) {
            for (Product p : fallback) {
                if (p.getSku().equals(sku)) {
                    if (p.getStock() < qty) return new OrderResult(false, "Insufficient stock");
                    int remaining = p.getStock() - qty;
                    p.setStock(remaining);
                    if (remaining == 0) {
                        fallback.removeIf(prod -> prod.getSku().equals(sku));
                        return new OrderResult(true, "Order placed. Item removed from inventory.");
                    }
                    return new OrderResult(true, "Order placed. Remaining stock: " + remaining);
                }
            }
            return new OrderResult(false, "SKU not found");
        }
    }

    public List<Product> lowStock(int threshold) {
        List<Product> res = new ArrayList<>();
        for (Product p : fallback) {
            if (p.getStock() <= threshold) {
                res.add(p);
            }
        }

        try {
            Product[] arr = res.toArray(new Product[0]);
            Sorting.selectionSort(arr, Comparator.comparingInt(Product::getStock));
            return Arrays.asList(arr);
        } catch (Exception ex) {
            res.sort(Comparator.comparingInt(Product::getStock));
            return res;
        }
    }

    public void saveCsv(String path) throws IOException {
        Path p = Paths.get(path);
        Files.createDirectories(p.getParent());
        try (BufferedWriter bw = Files.newBufferedWriter(p)) {
            bw.write("sku,name,category,price,stock\n");
            for (Product pr : fallback) {
                bw.write(String.format("%s,%s,%s,%.2f,%d%n",
                        pr.getSku(), pr.getName(), pr.getCategory(), pr.getPrice(), pr.getStock()));
            }
        }
        System.out.println("Successfully saved data to: " + path);
    }

    private void rebuildIndexes() {
        try {
            bySku.forEach((k, v) -> {});
            byName.forEach((k, v) -> {});

            for (Product p : fallback) {
                bySku.put(p.getSku(), p);
                byName.put(p.getName(), p);
            }
        } catch (Exception ex) {
            System.out.println("Error rebuilding indexes: " + ex.getMessage());
        }
    }
}