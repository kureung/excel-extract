package io.github.kureung.csv;

import io.github.kureung.common.CellExtract;
import io.github.kureung.common.TableExtract;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvExtractorTest {
    @Test
    void extract() {
        File csv = new File("src/test/java/io/github/kureung/csv/csv_sample.csv");
        CsvExtractor sut = new CsvExtractor(csv);
        List<CsvSampleData> result = sut.execute(CsvSampleData.class);

        Assertions.assertThat(result).hasSize(2);
    }



    @TableExtract(
            verifySheetName = false,
            firstDataRowIndex = 1,
            terminateCondition= @TableExtract.Entry(columNumber = 1, isNull = true)
    )
    private static class CsvSampleData {
        @CellExtract(categoryIndex = "B1", categoryValue = "성명", firstDataIndex = "B2")
        private String name;
        @CellExtract(categoryIndex = "C1", categoryValue = "나이", firstDataIndex = "C2")
        private String age;

        public CsvSampleData() {
        }

        @Override
        public String toString() {
            return "CsvSampleData{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
