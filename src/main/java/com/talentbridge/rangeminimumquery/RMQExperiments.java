package com.talentbridge.rangeminimumquery;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author emirs
 */
class RMQExperiments {

    private final Random random = new Random();

    // Generate a random array of a given size
    private int[] generateRandomArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(100000);  // Random values in range [0, 100000]
        }
        return array;
    }

    // Generate a sorted array of a given size
    private int[] generateSortedArray(int size) {
        int[] array = generateRandomArray(size);
        Arrays.sort(array);
        return array;
    }

    // Generate a descending sorted array of a given size
    private int[] generateDescendingArray(int size) {
        int[] array = generateSortedArray(size);
        for (int i = 0; i < array.length / 2; i++) {
            int temp = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = temp;
        }
        return array;
    }

    // Measure initialization time for a specific technique
    private long measureInitTime(RangeMinimumQuery rmq, String technique, int[] array) {
        long startTime = System.nanoTime();

        switch (technique) {
            case "Precompute All":
                rmq.preprocessPrecomputeAll(array);
                break;
            case "Sparse Table":
                rmq.preprocessSparseTable(array);
                break;
            case "Blocking":
                rmq.preprocessBlocking(array);
                break;
        }

        long endTime = System.nanoTime();
        return (endTime - startTime); // Time in nanoseconds
    }

    // Run queries and measure execution time
    private long measureQueryTime(RangeMinimumQuery rmq, String technique, int[] array, int queryCount) {
        long startTime = System.nanoTime();

        for (int q = 0; q < queryCount; q++) {
            int i = random.nextInt(array.length);
            int j = random.nextInt(array.length);
            if (i > j) {
                int temp = i;
                i = j;
                j = temp;
            }

            switch (technique) {
                case "Precompute All":
                    rmq.queryPrecomputeAll(i, j);
                    break;
                case "Sparse Table":
                    rmq.querySparseTable(i, j);
                    break;
                case "Blocking":
                    rmq.queryBlocking(array, i, j);
                    break;
                case "Precompute None":
                    rmq.queryPrecomputeNone(array, i, j);
                    break;
            }
        }

        long endTime = System.nanoTime();
        return (endTime - startTime); // Time in nanoseconds
    }

    // Test 1: Constant array size, varying query count
    public void testVaryingQueryCount(int arraySize, int[] queryCounts) {
        System.out.println("Experiment 1: Constant array size, varying query count\n");
        int[] array = generateRandomArray(arraySize);
        RangeMinimumQuery rmq = new RangeMinimumQuery();

        for (int queryCount : queryCounts) {
            System.out.println("Query Count: " + queryCount);

            for (String technique : Arrays.asList("Precompute All", "Sparse Table", "Blocking", "Precompute None")) {
                long initTime = technique.equals("Precompute None") ? 0 : measureInitTime(rmq, technique, array);
                long queryTime = measureQueryTime(rmq, technique, array, queryCount);
                long totalTime = initTime + queryTime;

                System.out.printf("  %s: Init Time = %d ns, Query Time = %d ns, Total Time = %d ns\n",
                        technique, initTime, queryTime, totalTime);
            }
        }
    }

    // Test 2: Constant query count, varying array size
    public void testVaryingArraySize(int queryCount, int[] arraySizes) {
        System.out.println("Experiment 2: Constant query count, varying array size\n");
        RangeMinimumQuery rmq = new RangeMinimumQuery();

        for (int arraySize : arraySizes) {
            int[] array = generateRandomArray(arraySize);
            System.out.println("Array Size: " + arraySize);

            for (String technique : Arrays.asList("Precompute All", "Sparse Table", "Blocking", "Precompute None")) {
                long initTime = technique.equals("Precompute None") ? 0 : measureInitTime(rmq, technique, array);
                long queryTime = measureQueryTime(rmq, technique, array, queryCount);
                long totalTime = initTime + queryTime;

                System.out.printf("  %s: Init Time = %d ns, Query Time = %d ns, Total Time = %d ns\n",
                        technique, initTime, queryTime, totalTime);
            }
        }
    }

    // Test 3: Constant array size and query count, varying array types
    public void testVaryingArrayTypes(int arraySize, int queryCount) {
        System.out.println("Experiment 3: Constant array size and query count, varying array types\n");
        RangeMinimumQuery rmq = new RangeMinimumQuery();

        Map<String, int[]> arrayTypes = new HashMap<>();
        arrayTypes.put("Random", generateRandomArray(arraySize));
        arrayTypes.put("Sorted", generateSortedArray(arraySize));
        arrayTypes.put("Descending", generateDescendingArray(arraySize));

        for (Map.Entry<String, int[]> entry : arrayTypes.entrySet()) {
            String arrayType = entry.getKey();
            int[] array = entry.getValue();

            System.out.println("Array Type: " + arrayType);

            for (String technique : Arrays.asList("Precompute All", "Sparse Table", "Blocking", "Precompute None")) {
                long initTime = technique.equals("Precompute None") ? 0 : measureInitTime(rmq, technique, array);
                long queryTime = measureQueryTime(rmq, technique, array, queryCount);
                long totalTime = initTime + queryTime;

                System.out.printf("  %s: Init Time = %d ns, Query Time = %d ns, Total Time = %d ns\n",
                        technique, initTime, queryTime, totalTime);
            }
        }
    }
}
