package io.github.kureung.common;

public class CellInformation {
    private final CellIndexName cellIndexName;
    private final String cellValue;

    private CellInformation(final CellIndexName cellIndexName, final String cellValue) {
        this.cellIndexName = cellIndexName;
        this.cellValue = cellValue;
    }

    public CellInformation(String cellIndexName, String cellValue) {
        this(new CellIndexName(cellIndexName), cellValue);
    }
}
