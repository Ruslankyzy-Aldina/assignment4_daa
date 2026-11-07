package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class NaiveSearch implements StringSearch {
    @Override
    public List<Integer> search(String text, String pattern, OperationCounter counter) {
        Objects.requireNonNull(text);
        Objects.requireNonNull(pattern);
        Objects.requireNonNull(counter);
        if (pattern.isEmpty()) throw new IllegalArgumentException("Pattern must not be empty");
        List<Integer> matches = new ArrayList<>();
        for (int i = 0; i <= text.length() - pattern.length(); i++) {
            int j = 0;
            while (j < pattern.length()) {
                counter.increment();
                if (text.charAt(i + j) != pattern.charAt(j)) break;
                j++;
            }
            if (j == pattern.length()) matches.add(i);
        }
        return matches;
    }
}
