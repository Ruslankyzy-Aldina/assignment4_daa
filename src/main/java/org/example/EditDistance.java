package org.example;

@FunctionalInterface
public interface EditDistance {
    int distance(String a, String b);
}
