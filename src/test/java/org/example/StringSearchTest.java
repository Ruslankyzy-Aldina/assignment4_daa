package org.example;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

class StringSearchTest {
    private final NaiveSearch naive = new NaiveSearch();
    private final KmpSearch kmp = new KmpSearch();

    @Test
    void referenceLpsArrays() {
        assertArrayEquals(new int[] {0, 1, 2, 0, 1, 2, 3, 3}, kmp.lps("AAACAAAA"));
        assertArrayEquals(new int[] {0, 0, 1, 2, 0, 1, 2, 3, 4}, kmp.lps("ABABCABAB"));
    }

    @Test
    void referenceMatchesAndOverlaps() {
        for (StringSearch search : List.of(naive, kmp)) {
            assertEquals(List.of(10), search.search("ABABDABACDABABCABAB", "ABABCABAB"));
            assertEquals(List.of(0, 1, 2, 3), search.search("AAAAA", "AA"));
            assertEquals(List.of(0, 2, 4), search.search("ABABABA", "ABA"));
            assertEquals(List.of(), search.search("abc", "abcd"));
            assertEquals(List.of(), search.search("abc", "z"));
            assertThrows(IllegalArgumentException.class, () -> search.search("abc", ""));
        }
    }

    @Test
    void oneHundredRandomTextsAgree() {
        Random random = new Random(20261107L);
        for (int testCase = 0; testCase < 100; testCase++) {
            String text = randomString(random, random.nextInt(30));
            String pattern = randomString(random, 1 + random.nextInt(7));
            assertEquals(naive.search(text, pattern), kmp.search(text, pattern),
                    () -> "Mismatch for text=" + text + ", pattern=" + pattern);
        }
    }

    private static String randomString(Random random, int length) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) result.append("abc".charAt(random.nextInt(3)));
        return result.toString();
    }
}
