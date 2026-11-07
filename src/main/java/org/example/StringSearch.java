package org.example;

import java.util.List;

@FunctionalInterface
public interface StringSearch {
    List<Integer> search(String text, String pattern);
}
