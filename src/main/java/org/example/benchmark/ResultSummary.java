package org.example.benchmark;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ResultSummary {
    private ResultSummary() {
    }

    public static void main(String[] args) throws IOException {
        Path input = args.length == 0 ? Path.of("results", "results.csv") : Path.of(args[0]);
        Map<Key, List<Double>> times = new HashMap<>();
        List<String> lines = Files.readAllLines(input, StandardCharsets.UTF_8);
        for (int i = 1; i < lines.size(); i++) {
            String[] fields = lines.get(i).split(",");
            Key key = new Key(fields[0], fields[1], Integer.parseInt(fields[2]));
            times.computeIfAbsent(key, ignored -> new ArrayList<>()).add(Double.parseDouble(fields[4]));
        }
        System.out.println("algorithm,input,n,median_time_ms");
        times.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(Key::algorithm)
                        .thenComparing(Key::input).thenComparingInt(Key::n)))
                .forEach(entry -> System.out.printf(java.util.Locale.ROOT, "%s,%s,%d,%.6f%n",
                        entry.getKey().algorithm(), entry.getKey().input(), entry.getKey().n(), median(entry.getValue())));
    }

    private static double median(List<Double> values) {
        values.sort(Double::compareTo);
        int middle = values.size() / 2;
        return values.size() % 2 == 0 ? (values.get(middle - 1) + values.get(middle)) / 2.0 : values.get(middle);
    }

    private record Key(String algorithm, String input, int n) {
    }
}
