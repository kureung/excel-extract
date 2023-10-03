package io.github.kureung.xls;

import io.github.kureung.common.CellExtract;
import io.github.kureung.common.TableExtract;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

class XlsExtractorTest {
    @Test
    void extract() {
        File Xls = new File("src/test/java/io/github/kureung/xls/xls_sample.Xls");

        XlsExtractor sut = new XlsExtractor(Xls);
        List<XlsSampleData> result = sut.execute(XlsSampleData.class);

        Assertions.assertThat(result).hasSize(2);
    }
    @TableExtract(
            sheetNumber = 0,
            sheetName="사원명부",
            verifySheetName = true,
            firstDataRowIndex = 3,
            terminateCondition= @TableExtract.Entry(columNumber = 1, isNull = true)
    )
    private static class XlsSampleData {
        @CellExtract(categoryIndex = "B3", categoryValue = "성명", firstDataIndex = "B4")
        private String name;
        @CellExtract(categoryIndex = "C3", categoryValue = "나이", firstDataIndex = "C4")
        private String age;

        public XlsSampleData() {
        }

        @Override
        public String toString() {
            return "XlsSampleData{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
