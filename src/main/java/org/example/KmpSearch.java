package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class KmpSearch implements StringSearch {
    public int[] lps(String pattern) {
        Objects.requireNonNull(pattern);
        int[] lps = new int[pattern.length()];
        for (int i = 1, length = 0; i < pattern.length();) {
            if (pattern.charAt(i) == pattern.charAt(length)) lps[i++] = ++length;
            else if (length > 0) length = lps[length - 1];
            else lps[i++] = 0;
        }
        return lps;
    }

    @Override
    public List<Integer> search(String text, String pattern) {
        Objects.requireNonNull(text);
        Objects.requireNonNull(pattern);
        if (pattern.isEmpty()) throw new IllegalArgumentException("Pattern must not be empty");
        List<Integer> matches = new ArrayList<>();
        if (pattern.length() > text.length()) return matches;
        int[] lps = lps(pattern);
        for (int i = 0, j = 0; i < text.length();) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                if (j == pattern.length()) {
                    matches.add(i - j);
                    j = lps[j - 1];
                }
            } else if (j > 0) j = lps[j - 1];
            else i++;
        }
        return matches;
    }
}
