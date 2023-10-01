package io.github.kureung.common;

import java.util.ArrayList;
import java.util.List;

public class VerificationInventory {
    private final List<CellInformation> elements;

    public VerificationInventory() {
        this.elements = new ArrayList<>();
    }

    public void addElement(String cellIndexName, String cellValue) {
        elements.add(new CellInformation(cellIndexName, cellValue));
    }

    public List<CellInformation> elements() {
        return new ArrayList<>(elements);
    }
}
