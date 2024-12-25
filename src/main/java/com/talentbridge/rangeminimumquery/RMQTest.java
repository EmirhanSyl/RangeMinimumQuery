package com.talentbridge.rangeminimumquery;

/**
 *
 * @author emirs
 */
public class RMQTest {

    public static void main(String[] args) {
        RMQExperiments experiments = new RMQExperiments();

        // Experiment 1: Constant array size, varying query count
        experiments.testVaryingQueryCount(10000, new int[]{1, 10, 100, 1000, 10000});

        // Experiment 2: Constant query count, varying array size
        experiments.testVaryingArraySize(100, new int[]{100, 1000, 10000, 10000});

        // Experiment 3: Constant array size and query count, varying array type
        experiments.testVaryingArrayTypes(10000, 1000);
    }
}
