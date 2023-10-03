package io.github.kureung.xls;

import io.github.kureung.common.CellExtract;
import io.github.kureung.common.CellIndexName;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.lang.reflect.Field;

public class XlsCategoryValidator<T> {
    private final Class<T> clazz;
    private final Sheet sheet;

    public XlsCategoryValidator(final Class<T> clazz, final Sheet sheet) {
        this.clazz = clazz;
        this.sheet = sheet;
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
        for (Row row : sheet) {
            verifyRowNumber(cellIndexName, cellValue, row);
        }
    }

    private void verifyRowNumber(CellIndexName cellIndexName, String cellValue, Row row) {
        if (row.getRowNum() == cellIndexName.yCoordinate()) {
            Cell cell = row.getCell(cellIndexName.xCoordinate());
            cell.getStringCellValue();
            verifyValue(cellValue, cell);
        }
    }

    private void verifyValue(String cellValue, Cell cell) {
        DataFormatter formatter = new DataFormatter();
        String actualCellValue = formatter.formatCellValue(cell);
        if (!cellValue.equals(actualCellValue)) {
            String message = String.format("셀 값 검증 실패. expected=%s, actual=%s", cellValue, actualCellValue);
            throw new IllegalStateException(message);
        }
    }
}
