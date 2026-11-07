package org.example;

import java.util.Objects;

public final class TwoRowEditDistance implements EditDistance {
    @Override
    public int distance(String a, String b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);
        int m = b.length();
        int[] previous = new int[m + 1];
        int[] current = new int[m + 1];
        for (int j = 0; j <= m; j++) previous[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            current[0] = i;
            for (int j = 1; j <= m; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) current[j] = previous[j - 1];
                else current[j] = 1 + Math.min(previous[j],
                        Math.min(current[j - 1], previous[j - 1]));
            }
            int[] swap = previous;
            previous = current;
            current = swap;
        }
        return previous[m];
    }
}
