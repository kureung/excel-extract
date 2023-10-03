package io.github.kureung.xlsx;

import io.github.kureung.common.TableExtract;
import org.dhatim.fastexcel.reader.CellType;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class XlsxTableExtractValidator<T> {
    private final File file;
    private final Class<T> clazz;

    public XlsxTableExtractValidator(final File file, final Class<T> clazz) {
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
        int firstValidDataRowNumber = table.firstDataRowIndex() + 1;
        List<Row> tableRows = tableRows(validSheet(), firstValidDataRowNumber);
        List<Row> result = new ArrayList<>();
        for (Row row : tableRows) {
            if (table.terminateCondition().columNumber() >= 0) {
                TableExtract.Entry entry = table.terminateCondition();
                int columNumber = entry.columNumber();
                if (entry.isNull()) {
                    if (!row.hasCell(columNumber) || row.getCell(columNumber).getType() == CellType.EMPTY) {
                        return result;
                    }
                }
                if (row.getCell(columNumber).getText().equals(entry.value())) {
                    return result;
                }
            }
            result.add(row);
        }
        return result;
    }

    private List<Row> tableRows(Sheet sheet, int firstValidDataRowNumber) {
        try {
            return sheet.read()
                    .stream()
                    .filter(row -> row.getRowNum() >= firstValidDataRowNumber)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        try {
            return new ReadableWorkbook(file)
                    .getSheet(sheetPage)
                    .orElseThrow(() -> new IllegalStateException("not found sheet"));
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
        if (!sheetName.equals(sheet.getName())) {
            String message = String.format("시트 이름 불일치. expected=%s, actual=%s", sheetName, sheet.getName());
            throw new IllegalStateException(message);
        }
    }
}
