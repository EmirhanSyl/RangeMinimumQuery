package com.talentbridge.rangeminimumquery;

/**
 *
 * @author emirs
 */
public class RangeMinimumQuery {

    // Technique 1: Precompute All
    private int[][] precomputeAll;

    public void preprocessPrecomputeAll(int[] array) {
        int n = array.length;
        precomputeAll = new int[n][n];
        for (int i = 0; i < n; i++) {
            precomputeAll[i][i] = array[i];
            for (int j = i + 1; j < n; j++) {
                precomputeAll[i][j] = Math.min(precomputeAll[i][j - 1], array[j]);
            }
        }
    }

    public int queryPrecomputeAll(int i, int j) {
        return precomputeAll[i][j];
    }

    // Technique 2: Sparse Table
    private int[][] sparseTable;

    public void preprocessSparseTable(int[] array) {
        int n = array.length;
        int log = (int) (Math.log(n) / Math.log(2)) + 1;
        sparseTable = new int[n][log];

        for (int i = 0; i < n; i++) {
            sparseTable[i][0] = array[i];
        }

        for (int j = 1; (1 << j) <= n; j++) {
            for (int i = 0; (i + (1 << j)) <= n; i++) {
                sparseTable[i][j] = Math.min(sparseTable[i][j - 1], sparseTable[i + (1 << (j - 1))][j - 1]);
            }
        }
    }

    public int querySparseTable(int i, int j) {
        int k = (int) (Math.log(j - i + 1) / Math.log(2));
        return Math.min(sparseTable[i][k], sparseTable[j - (1 << k) + 1][k]);
    }

    // Technique 3: Blocking
    private int[] blockMin;
    private int blockSize;

    public void preprocessBlocking(int[] array) {
        int n = array.length;
        blockSize = (int) Math.ceil(Math.sqrt(n));
        int numBlocks = (int) Math.ceil((double) n / blockSize);
        blockMin = new int[numBlocks];

        for (int i = 0; i < numBlocks; i++) {
            blockMin[i] = Integer.MAX_VALUE;
            for (int j = i * blockSize; j < Math.min((i + 1) * blockSize, n); j++) {
                blockMin[i] = Math.min(blockMin[i], array[j]);
            }
        }
    }

    public int queryBlocking(int[] array, int i, int j) {
        int leftBlock = i / blockSize;
        int rightBlock = j / blockSize;

        if (leftBlock == rightBlock) {
            int min = Integer.MAX_VALUE;
            for (int k = i; k <= j; k++) {
                min = Math.min(min, array[k]);
            }
            return min;
        }

        int leftMin = Integer.MAX_VALUE;
        for (int k = i; k < (leftBlock + 1) * blockSize; k++) {
            leftMin = Math.min(leftMin, array[k]);
        }

        int rightMin = Integer.MAX_VALUE;
        for (int k = rightBlock * blockSize; k <= j; k++) {
            rightMin = Math.min(rightMin, array[k]);
        }

        int middleMin = Integer.MAX_VALUE;
        for (int k = leftBlock + 1; k < rightBlock; k++) {
            middleMin = Math.min(middleMin, blockMin[k]);
        }

        return Math.min(Math.min(leftMin, rightMin), middleMin);
    }

    // Technique 4: Precompute None
    public int queryPrecomputeNone(int[] array, int i, int j) {
        int min = Integer.MAX_VALUE;
        for (int k = i; k <= j; k++) {
            min = Math.min(min, array[k]);
        }
        return min;
    }
}

