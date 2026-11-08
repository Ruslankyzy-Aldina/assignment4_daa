package org.example;

import java.io.IOException;
import org.example.benchmark.Benchmark;
import org.example.benchmark.ResultSummary;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) throws IOException {
        String command = args.length == 0 ? "help" : args[0].toLowerCase();
        String[] commandArgs = new String[Math.max(0, args.length - 1)];
        if (args.length > 1) System.arraycopy(args, 1, commandArgs, 0, args.length - 1);
        switch (command) {
            case "benchmark" -> Benchmark.main(commandArgs);
            case "summary" -> ResultSummary.main(commandArgs);
            case "help", "--help", "-h" -> printHelp();
            default -> {
                System.err.println("Unknown command: " + args[0]);
                printHelp();
                System.exit(1);
            }
        }
    }

    private static void printHelp() {
        System.out.println("Assignment 4: Dynamic Programming and String Algorithms");
        System.out.println();
        System.out.println("Commands:");
        System.out.println("  help                 Show this help message");
        System.out.println("  benchmark            Run the reproducible benchmark");
        System.out.println("  summary              Print median times from results/results.csv");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  java -cp target/classes org.example.Main benchmark");
        System.out.println("  java -cp target/classes org.example.Main summary");
        System.out.println();
        System.out.println("Project areas:");
        System.out.println("  editdistance         Recursive, table, and two-row Levenshtein distance");
        System.out.println("  search               Naive and KMP string matching");
        System.out.println("  benchmark            Counters, CSV generation, and result summaries");
    }
}
