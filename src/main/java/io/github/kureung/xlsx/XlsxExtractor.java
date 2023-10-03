package io.github.kureung.xlsx;

import io.github.kureung.common.CellExtract;
import io.github.kureung.common.CellIndexName;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.Row;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class XlsxExtractor {
    private final File file;

    public XlsxExtractor(final File file) {
        this.file = file;
    }

    public <T> List<T> execute(Class<T> clazz) {
        List<Row> rows = rows(file, clazz);
        verifyCategoryValue(clazz, rows);
        return extract(clazz, rows);
    }

    private <T> void verifyCategoryValue(Class<T> clazz, List<Row> rows) {
        new XlsxCategoryValidator<>(clazz, rows).execute();
    }

    private <T> List<Row> rows(File file, Class<T> clazz) {
        return new XlsxTableExtractValidator<>(file, clazz).validRows();
    }

    private <T> List<T> extract(final Class<T> clazz, final List<Row> rows) {
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

    private int xCoordinate(String cellIndexName) {
        return new CellIndexName(cellIndexName)
                .xCoordinate();
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
