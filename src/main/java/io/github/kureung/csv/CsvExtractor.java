package io.github.kureung.csv;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import io.github.kureung.common.CellExtract;
import io.github.kureung.common.CellIndexName;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class CsvExtractor {
    private final File file;

    public CsvExtractor(final File file) {
        this.file = file;
    }

    public <T> List<T> execute(Class<T> clazz) {
        verifyCategoryValue(clazz);
        List<String[]> rows = rows(file, clazz);
        return extract(clazz, rows);
    }

    private <T> void verifyCategoryValue(Class<T> clazz) {
        new CsvCategoryValidator<>(clazz, allRows()).execute();
    }

    private List<String[]> allRows() {
        File file = new File("src/test/java/io/github/kureung/csv/csv_sample.csv");
        try {
            CSVReader csvReader = new CSVReader(new InputStreamReader(Files.newInputStream(file.toPath()), "EUC-KR"));
            return csvReader.readAll();
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> List<String[]> rows(File file, Class<T> clazz) {
        return new CsvTableExtractValidator<>(file, clazz).validRows();
    }

    private <T> List<T> extract(final Class<T> clazz, final List<String[]> rows) {
        List<T> result = new ArrayList<>();

        Field[] fields = clazz.getDeclaredFields();

        for (String[] row : rows) {
            T instance = newInstance(clazz);
            for (Field field : fields) {
                if (field.isAnnotationPresent(CellExtract.class)) {
                    CellExtract extractionCondition = field.getAnnotation(CellExtract.class);
                    int x = xCoordinate(extractionCondition.firstDataIndex());
                    String cell = row[x];
                    injectField(field, instance, cell);
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
