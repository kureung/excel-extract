package io.github.kureung.csv;

import io.github.kureung.common.CellExtract;
import io.github.kureung.common.CellIndexName;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

public class CsvCategoryValidator<T> {
    private final Class<T> clazz;
    private final List<String[]> rows;

    public CsvCategoryValidator(final Class<T> clazz, final List<String[]> rows) {
        this.clazz = clazz;
        this.rows = rows;
    }

    void execute() {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            checkHasCellExtractAnnotation(field);
        }
    }

    private void checkHasCellExtractAnnotation(Field field) {
        if (field.isAnnotationPresent(CellExtract.class)) {
            CellExtract extractionCondition = field.getAnnotation(CellExtract.class);
            checkHasVerifyList(extractionCondition);
        }
    }

    private void checkHasVerifyList(CellExtract extractionCondition) {
        if (extractionCondition.verifyCellCategory()) {
            String categoryIndexName = extractionCondition.categoryIndex();
            CellIndexName cellIndexName = new CellIndexName(categoryIndexName);
            String cellValue = extractionCondition.categoryValue();
            rowTraversal(cellIndexName, cellValue);
        }
    }

    private void rowTraversal(CellIndexName cellIndexName, String cellValue) {
        for (int i = 0; i < rows.size(); i++) {
            if (i == cellIndexName.yCoordinate()) {
                String[] row = rows.get(i);
                String actualCellValue = row[cellIndexName.xCoordinate()];
                verifyValue(cellValue, actualCellValue);
            }
        }
    }

    private void verifyValue(final String cellValue, final String actualCellValue) {
        if (!Objects.equals(cellValue, actualCellValue)) {
            String message = String.format("셀 값 검증 실패. expected=%s, actual=%s", cellValue, actualCellValue);
            throw new IllegalStateException(message);
        }
    }
}
