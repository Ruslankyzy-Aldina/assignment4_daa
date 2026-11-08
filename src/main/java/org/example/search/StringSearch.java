package org.example.search;

import org.example.benchmark.OperationCounter;

import java.util.List;

@FunctionalInterface
public interface StringSearch {
    List<Integer> search(String text, String pattern, OperationCounter counter);

    default List<Integer> search(String text, String pattern) {
        return search(text, pattern, new OperationCounter());
    }
}
