# Introduction

This is a console-based ecommerce inventory management system, developed under specific constraints to demonstrate the core application of data structures and algorithms.

The system employs a singly linked list as the primary storage structure for product data to facilitate efficient insertion and deletion operations.

It integrates a hash table, utilizing separate chaining and rehashing mechanisms, to enable fast retrieval based on SKU and product name.

Furthermore, various sorting algorithms—including Selection Sort, Merge Sort, and Quick Sort—are applied to meet the requirements for listing products and generating reports in different scenarios.

This project serves as a practical case study that validates the effectiveness of classical data structures and algorithms within a functional software system.

# Demo Video

The program demonstration video will be included in the entire zip file.

Also you can down it by Baidu Netdisk：demo_video_24107715.mp4 Link: https://pan.baidu.com/s/1YVSs6o5d1qo9Tm9XUCpVBQ Password: 5enx

# Notes

In addition to the three sorting Java classes, I have made fine adjustments to the number of repetitions in the Analyzer, changing it from 100 to 5, to resolve the issue where the time complexity of different sorting methods did not follow the expected patterns (you can restore it to 100 by uncommenting to verify). Following the original code structure, I added methods to test the execution time of the three sorting classes in the Sorting class (but you need to uncomment them if you want to check them).

Additionally, I have slightly modified the InventoryService, resolved the TODO items, and improved the original functionality.