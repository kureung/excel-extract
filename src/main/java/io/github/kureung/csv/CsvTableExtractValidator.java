package io.github.kureung.csv;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import io.github.kureung.common.TableExtract;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

class CsvTableExtractValidator<T> {
    private final File file;
    private final Class<T> clazz;

    public CsvTableExtractValidator(final File file, final Class<T> clazz) {
        this.file = file;
        this.clazz = clazz;
    }

    public List<String[]> validRows() {
        if (!clazz.isAnnotationPresent(TableExtract.class)) {
            throw new IllegalStateException("need TableExtract Annotation");
        }

        TableExtract table = clazz.getAnnotation(TableExtract.class);
        int firstValidDataRowNumber = table.firstDataRowIndex();
        List<String[]> result = new ArrayList<>();
        List<String[]> rows = tableRows();

        for (int i = 0; i < rows.size(); i++) {
            if (i < firstValidDataRowNumber) {
                continue;
            }

            if (table.terminateCondition().columNumber() >= 0) {
                TableExtract.Entry entry = table.terminateCondition();
                int columNumber = entry.columNumber();
                String cell = rows.get(i)[columNumber];
                if (entry.isNull()) {
                    if (cell == null || cell.isEmpty()) {
                        return result;
                    }
                }
                if (cell.equals(entry.value())) {
                    return result;
                }
            }
            result.add(rows.get(i));
        }
        return result;
    }

    private List<String[]> tableRows() {
        try (InputStreamReader is = new InputStreamReader(Files.newInputStream(file.toPath()), "EUC-KR")){
            CSVReader csvReader = new CSVReader(is);
            return csvReader.readAll();
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }
}
