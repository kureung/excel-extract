package io.github.kureung.common;

import org.apache.poi.ss.util.CellReference;

import java.util.regex.Pattern;

public class CellIndexName {
    private final String value;

    public CellIndexName(final String value) {
        verify(value);
        this.value = value;
    }

    private static void verify(final String value) {
        if (value == null || !Pattern.matches("[A-Za-z]+[0-9]+", value)) {
            throw new IllegalArgumentException("셀 좌표 이름은 '[A-Za-z]+[0-9]+' 양식이어야 합니다.");
        }
    }
}
