package org.example.editdistance;

import java.util.Objects;
import org.example.benchmark.OperationCounter;

public final class RecursiveEditDistance implements EditDistance {
    @Override
    public int distance(String a, String b, OperationCounter counter) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);
        Objects.requireNonNull(counter);
        return solve(a, b, a.length(), b.length(), counter);
    }

    private int solve(String a, String b, int n, int m, OperationCounter counter) {
        counter.increment();
        if (n == 0) return m;
        if (m == 0) return n;
        if (a.charAt(n - 1) == b.charAt(m - 1)) return solve(a, b, n - 1, m - 1, counter);
        return 1 + Math.min(solve(a, b, n - 1, m, counter),
                Math.min(solve(a, b, n, m - 1, counter), solve(a, b, n - 1, m - 1, counter)));
    }
}
