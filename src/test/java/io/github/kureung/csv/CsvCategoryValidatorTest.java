package io.github.kureung.csv;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import io.github.kureung.common.CellExtract;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CsvCategoryValidatorTest {
    @Test
    void validCategory_is_not_throw_exception() throws IOException, CsvException {
        CsvCategoryValidator<CsvValidCategorySample> sut = new CsvCategoryValidator<>(CsvValidCategorySample.class, rows());

        assertThatCode(() -> sut.execute())
                .doesNotThrowAnyException();
    }

    private List<String[]> rows() throws IOException, CsvException {
        File file = new File("src/test/java/io/github/kureung/csv/csv_sample.csv");
        CSVReader csvReader = new CSVReader(new InputStreamReader(Files.newInputStream(file.toPath()), "EUC-KR"));
        return csvReader.readAll();
    }

    private static class CsvValidCategorySample {
        @CellExtract(categoryIndex = "B1", categoryValue = "성명", firstDataIndex = "B2")
        private String name;
    }

    @Test
    void invalidCategory_is_throw_exception() throws IOException, CsvException {
        CsvCategoryValidator<CsvInvalidCategorySample> sut = new CsvCategoryValidator<>(CsvInvalidCategorySample.class, rows());
        assertThatThrownBy(() -> sut.execute())
                .isInstanceOf(IllegalStateException.class);
    }

    private static class CsvInvalidCategorySample {
        @CellExtract(categoryIndex = "B1", categoryValue = "성명1", firstDataIndex = "B2")
        private String name;
    }
}
