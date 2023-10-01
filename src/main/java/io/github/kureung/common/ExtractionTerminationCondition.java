package io.github.kureung.common;

public class ExtractionTerminationCondition {
    private final int columnIndex;
    private final String value;

    public ExtractionTerminationCondition(final int columnIndex, final String value) {
        if (columnIndex < 0) {
            throw new IllegalArgumentException("columnIndex can not negative");
        }

        this.columnIndex = columnIndex;
        this.value = value;
    }

    public int columnIndex() {
        return columnIndex;
    }

    public String value() {
        return value;
    }
}
