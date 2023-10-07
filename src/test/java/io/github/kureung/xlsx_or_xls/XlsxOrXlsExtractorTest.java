package io.github.kureung.xlsx_or_xls;

import io.github.kureung.common.CellExtract;
import io.github.kureung.common.TableExtract;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

class XlsxOrXlsExtractorTest {
    @Test
    void extract() {
        File xlsx = new File("src/test/java/io/github/kureung/xlsx_or_xls/xlsx_sample.xlsx");

        XlsxOrXlsExtractor sut = new XlsxOrXlsExtractor(xlsx);
        List<XlsxSampleData> result = sut.execute(XlsxSampleData.class);

        Assertions.assertThat(result).hasSize(2);
    }

    @TableExtract(
            sheetNumber = 0,
            sheetName="사원명부",
            verifySheetName = true,
            firstDataRowIndex = 3,
            terminateCondition= @TableExtract.Entry(columNumber = 1, isNull = true)
    )
    private static class XlsxSampleData {
        @CellExtract(categoryIndex = "B3", categoryValue = "성명", firstDataIndex = "B4")
        private String name;
        @CellExtract(categoryIndex = "C3", categoryValue = "나이", firstDataIndex = "C4")
        private String age;

        public XlsxSampleData() {
        }

        @Override
        public String toString() {
            return "XlsxSampleData{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}


