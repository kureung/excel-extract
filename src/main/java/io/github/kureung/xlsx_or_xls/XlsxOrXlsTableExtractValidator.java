package io.github.kureung.xlsx_or_xls;

import io.github.kureung.common.TableExtract;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class XlsxOrXlsTableExtractValidator<T> {
    private final File file;
    private final Class<T> clazz;

    public XlsxOrXlsTableExtractValidator(final File file, final Class<T> clazz) {
        this.file = file;
        this.clazz = clazz;
    }

    public Sheet validSheet() {
        verifyHasTableExtractAnnotation();
        Sheet sheet = extractSheet();
        verifySheetName(sheet);
        return sheet;
    }

    public List<Row> validRows() {
        TableExtract table = clazz.getAnnotation(TableExtract.class);
        int firstValidDataRowNumber = table.firstDataRowIndex();
        List<Row> tableRows = tableRows(validSheet(), firstValidDataRowNumber);

        List<Row> result = new ArrayList<>();
        for (Row row : tableRows) {
            if (table.terminateCondition().columNumber() >= 0) {
                TableExtract.Entry entry = table.terminateCondition();
                int columNumber = entry.columNumber();
                if (entry.isNull()) {
                    Cell cell = row.getCell(columNumber);
                    if (cell == null || cell.getCellType() == CellType._NONE || cell.getCellType() == CellType.BLANK) {
                        return result;
                    }
                }
                DataFormatter formatter = new DataFormatter();
                String actualCellValue = formatter.formatCellValue(row.getCell(columNumber));
                if (actualCellValue.equals(entry.value())) {
                    return result;
                }
            }
            result.add(row);
        }
        return result;
    }

    private List<Row> tableRows(Sheet sheet, int firstValidDataRowNumber) {
        List<Row> result = new ArrayList<>();
        for (Row row : sheet) {
            if (row.getRowNum() >= firstValidDataRowNumber) {
                result.add(row);
            }
        }
        return result;
    }

    private void verifyHasTableExtractAnnotation() {
        if (!clazz.isAnnotationPresent(TableExtract.class)) {
            throw new IllegalStateException("need TableExtract annotation");
        }
    }

    private Sheet extractSheet() {
        TableExtract table = clazz.getAnnotation(TableExtract.class);
        int sheetIndex = table.sheetNumber();
        return sheet(sheetIndex);
    }

    private Sheet sheet(int sheetPage) {
        try (FileInputStream fis = new FileInputStream(file)) {
            return WorkbookFactory.create(fis)
                    .getSheetAt(sheetPage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void verifySheetName(Sheet sheet) {
        TableExtract table = clazz.getAnnotation(TableExtract.class);
        if (table.verifySheetName()) {
            String sheetName = table.sheetName();
            verifyValue(sheet, sheetName);
        }
    }

    private void verifyValue(Sheet sheet, String sheetName) {
        if (!sheetName.equals(sheet.getSheetName())) {
            String message = String.format("시트 이름 불일치. expected=%s, actual=%s", sheetName, sheet.getSheetName());
            throw new IllegalStateException(message);
        }
    }
}
