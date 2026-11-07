package org.example;

public final class OperationCounter {
    private long value;

    public void increment() {
        value++;
    }

    public long value() {
        return value;
    }
}
