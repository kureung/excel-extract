package io.github.kureung.xlsx;

import io.github.kureung.common.CellExtract;
import io.github.kureung.common.CellIndexName;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.Row;

import java.lang.reflect.Field;
import java.util.List;

class CategoryValidator <T> {

    private final Class<T> clazz;
    private final List<Row> rows;

    CategoryValidator(final Class<T> clazz, final List<Row> rows) {
        this.clazz = clazz;
        this.rows = rows;
    }

    void execute() {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            verifyValue(field);
        }
    }

    private void verifyValue(Field field) {
        if (field.isAnnotationPresent(CellExtract.class)) {
            CellExtract extractionCondition = field.getAnnotation(CellExtract.class);
            extracted(extractionCondition);
        }
    }

    private void extracted(CellExtract extractionCondition) {
        if (extractionCondition.verifyCellCategory()) {
            String categoryIndexName = extractionCondition.categoryIndex();
            String cellValue = extractionCondition.categoryValue();
            CellIndexName cellIndexName = new CellIndexName(categoryIndexName);
            extracted2(cellIndexName, cellValue);
        }
    }

    private void extracted2(CellIndexName cellIndexName, String cellValue) {
        for (Row row : rows) {
            extracted3(cellIndexName, cellValue, row);
        }
    }

    private void extracted3(CellIndexName cellIndexName, String cellValue, Row row) {
        if (row.getRowNum() == cellIndexName.yCoordinate() + 1) {
            Cell cell = row.getCell(cellIndexName.xCoordinate());
            extracted4(cellValue, cell);
        }
    }

    private void extracted4(String cellValue, Cell cell) {
        if (!cellValue.equals(cell.getText())) {
            String message = String.format("셀 값 검증 실패. expected=%s, actual=%s", cellValue, cell.getText());
            throw new IllegalStateException(message);
        }
    }
}
