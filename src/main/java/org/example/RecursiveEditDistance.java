package org.example;

import java.util.Objects;

public final class RecursiveEditDistance implements EditDistance {
    @Override
    public int distance(String a, String b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);
        return solve(a, b, a.length(), b.length());
    }

    private int solve(String a, String b, int n, int m) {
        if (n == 0) return m;
        if (m == 0) return n;
        if (a.charAt(n - 1) == b.charAt(m - 1)) return solve(a, b, n - 1, m - 1);
        return 1 + Math.min(solve(a, b, n - 1, m),
                Math.min(solve(a, b, n, m - 1), solve(a, b, n - 1, m - 1)));
    }
}
