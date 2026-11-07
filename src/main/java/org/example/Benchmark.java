package org.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public final class Benchmark {
    private static final long SEED = 20261107L;
    private static final int RUNS = 5;
    private static volatile int checksum;

    private Benchmark() {
    }

    public static void main(String[] args) throws IOException {
        Path output = args.length == 0 ? Path.of("results", "results.csv") : Path.of(args[0]);
        if (output.getParent() != null) Files.createDirectories(output.getParent());
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(output, StandardCharsets.UTF_8))) {
            writer.println("algorithm,input,n,run,time_ms,operations,metric,result");
            runEditDistance(writer);
            runSearch(writer);
        }
        System.out.println("Saved " + output + " (checksum=" + checksum + ")");
    }

    private static void runEditDistance(PrintWriter writer) {
        List<NamedEdit> algorithms = List.of(
                new NamedEdit("recursive", new RecursiveEditDistance()),
                new NamedEdit("table", new TableEditDistance()),
                new NamedEdit("two_rows", new TwoRowEditDistance()));
        for (NamedEdit named : algorithms) {
            int[] sizes = named.name.equals("recursive")
                    ? new int[] {2, 4, 6, 8} : new int[] {64, 128, 256, 512};
            for (String input : List.of("random", "disjoint")) {
                for (int n : sizes) {
                    Random random = new Random(SEED + n);
                    String a = randomString(random, n, "abcd");
                    String b = randomString(random, n, input.equals("disjoint") ? "wxyz" : "abcd");
                    for (int warmup = 0; warmup < 2; warmup++) checksum ^= named.algorithm.distance(a, b);
                    for (int run = 1; run <= RUNS; run++) {
                        OperationCounter counter = new OperationCounter();
                        long start = System.nanoTime();
                        int result = named.algorithm.distance(a, b, counter);
                        long elapsed = System.nanoTime() - start;
                        checksum ^= result;
                        write(writer, named.name, input, n, run, elapsed, counter.value(),
                                "subproblems_or_cells", result);
                    }
                }
            }
        }
    }

    private static void runSearch(PrintWriter writer) {
        List<NamedSearch> algorithms = List.of(
                new NamedSearch("naive", new NaiveSearch()),
                new NamedSearch("kmp", new KmpSearch()));
        for (String input : List.of("random", "repeated_prefix")) {
            for (int n : new int[] {20_000, 40_000, 80_000, 160_000}) {
                Random random = new Random(SEED + n);
                String text = input.equals("random") ? randomString(random, n, "abcd") : "a".repeat(n);
                String pattern = input.equals("random") ? randomString(random, 32, "abcd")
                        : "a".repeat(31) + "b";
                List<Integer> expected = new NaiveSearch().search(text, pattern);
                for (NamedSearch named : algorithms) {
                    for (int warmup = 0; warmup < 2; warmup++) checksum ^= named.algorithm.search(text, pattern).size();
                    for (int run = 1; run <= RUNS; run++) {
                        OperationCounter counter = new OperationCounter();
                        long start = System.nanoTime();
                        List<Integer> result = named.algorithm.search(text, pattern, counter);
                        long elapsed = System.nanoTime() - start;
                        if (!result.equals(expected)) throw new IllegalStateException("Search results differ");
                        checksum ^= result.size();
                        write(writer, named.name, input, n, run, elapsed, counter.value(),
                                "character_comparisons", result.size());
                    }
                }
            }
        }
    }

    private static String randomString(Random random, int n, String alphabet) {
        StringBuilder result = new StringBuilder(n);
        for (int i = 0; i < n; i++) result.append(alphabet.charAt(random.nextInt(alphabet.length())));
        return result.toString();
    }

    private static void write(PrintWriter writer, String algorithm, String input, int n, int run,
                              long elapsedNs, long operations, String metric, int result) {
        writer.printf(Locale.ROOT, "%s,%s,%d,%d,%.6f,%d,%s,%d%n", algorithm, input, n,
                run, elapsedNs / 1_000_000.0, operations, metric, result);
    }

    private record NamedEdit(String name, EditDistance algorithm) { }
    private record NamedSearch(String name, StringSearch algorithm) { }
}
