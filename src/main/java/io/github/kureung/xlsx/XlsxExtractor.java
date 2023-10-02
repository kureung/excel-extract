package io.github.kureung.xlsx;

import io.github.kureung.common.CellExtract;
import io.github.kureung.common.CellIndexName;
import io.github.kureung.common.TableExtract;
import org.dhatim.fastexcel.reader.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class XlsxExtractor {
    private final File file;

    public XlsxExtractor(final File file) {
        this.file = file;
    }

    public <T> List<T> execute(Class<T> clazz) {
        List<Row> rows = tableRows(clazz);
        verifyCategoryValue(clazz, rows);

        List<T> result = new ArrayList<>();

        Field[] fields = clazz.getDeclaredFields();
        for (Row row : rows) {
            T instance = newInstance(clazz);
            for (Field field : fields) {
                if (field.isAnnotationPresent(CellExtract.class)) {
                    CellExtract extractionCondition = field.getAnnotation(CellExtract.class);
                    int x = xCoordinate(extractionCondition.firstDataIndex());
                    Cell cell = row.getCell(x);
                    injectField(field, instance, cell.getText());
                }
            }
            result.add(instance);
        }
        return result;
    }

    private <T> void verifyCategoryValue(Class<T> clazz, List<Row> rows) {
        new CategoryValidator<>(clazz, rows).execute();
    }

    private <T> List<Row> tableRows(Class<T> clazz) {
        Sheet sheet = validSheet(clazz);
        TableExtract table = clazz.getAnnotation(TableExtract.class);
        int firstValidDataRowNumber = table.firstDataRowIndex() + 1;
        List<Row> collect = validRows(sheet).stream()
                .filter(row -> row.getRowNum() >= firstValidDataRowNumber)
                .collect(Collectors.toList());

        List<Row> result = new ArrayList<>();
        for (Row row : collect) {
            if (table.terminateCondition().columNumber() >= 0) {
                TableExtract.Entry entry = table.terminateCondition();
                int columNumber = entry.columNumber();
                if (entry.isNull()) {
                    if (!row.hasCell(columNumber) || row.getCell(columNumber).getType() == CellType.EMPTY) {
                        return result;
                    }
                }
                if(row.getCell(columNumber).getText().equals(entry.value())) {
                    return result;
                }
            }
            result.add(row);
        }
        return result;
    }

    private <T> Sheet validSheet(Class<T> clazz) {
//        hasAnnotation ?
        if (!clazz.isAnnotationPresent(TableExtract.class)) {
            throw new IllegalStateException("need CellExtractionTable annotation");
        }

        TableExtract table = clazz.getAnnotation(TableExtract.class);
        int sheetIndex = table.sheetNumber();
        Sheet sheet = sheet(sheetIndex);

//         verify sheetName
        if (table.verifySheetName()) {
            String sheetName = table.sheetName();
            if (!sheetName.equals(sheet.getName())) {
                String message = String.format("시트 이름 불일치. expected=%s, actual=%s", sheetName, sheet.getName());
                throw new IllegalStateException(message);
            }
        }
        return sheet;
    }

    private List<Row> validRows(Sheet sheet) {
        try {
            return sheet.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int xCoordinate(String cellIndexName) {
        return new CellIndexName(cellIndexName)
                .xCoordinate();
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

    private <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void injectField(Field field, Object instance, String value) {
        field.setAccessible(true);
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
