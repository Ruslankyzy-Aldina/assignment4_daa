package org.example.editdistance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.example.benchmark.OperationCounter;

class EditDistanceTest {
    private final List<EditDistance> implementations = List.of(
            new RecursiveEditDistance(), new TableEditDistance(), new TwoRowEditDistance());

    @Test
    void referenceValuesAndEmptyStrings() {
        for (EditDistance implementation : implementations) {
            assertEquals(3, implementation.distance("kitten", "sitting"));
            assertEquals(2, implementation.distance("flaw", "lawn"));
            assertEquals(5, implementation.distance("intention", "execution"));
            assertEquals(3, implementation.distance("", "abc"));
            assertEquals(3, implementation.distance("abc", ""));
            assertEquals(0, implementation.distance("", ""));
            assertEquals(0, implementation.distance("abc", "abc"));
        }
    }

    @Test
    void oneHundredRandomPairsAgree() {
        Random random = new Random(20261107L);
        for (int testCase = 0; testCase < 100; testCase++) {
            String a = randomString(random);
            String b = randomString(random);
            int expected = implementations.get(0).distance(a, b);
            for (EditDistance implementation : implementations) {
                assertEquals(expected, implementation.distance(a, b),
                        () -> "Mismatch for a=" + a + ", b=" + b);
            }
        }
    }

    @Test
    void countersMeasureCallsAndCells() {
        OperationCounter recursive = new OperationCounter();
        OperationCounter table = new OperationCounter();
        OperationCounter twoRows = new OperationCounter();
        new RecursiveEditDistance().distance("ab", "cd", recursive);
        new TableEditDistance().distance("ab", "cd", table);
        new TwoRowEditDistance().distance("ab", "cd", twoRows);
        assertEquals(19, recursive.value());
        assertEquals(4, table.value());
        assertEquals(4, twoRows.value());
    }

    private static String randomString(Random random) {
        int length = random.nextInt(5);
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) result.append("abc".charAt(random.nextInt(3)));
        return result.toString();
    }
}
