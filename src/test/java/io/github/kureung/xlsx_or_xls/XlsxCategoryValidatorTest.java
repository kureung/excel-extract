package io.github.kureung.xlsx_or_xls;

import io.github.kureung.common.CellExtract;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;

class XlsxCategoryValidatorTest {
    @Test
    void validCategory_is_not_throw_exception() throws IOException {
        XlsxOrXlsCategoryValidator<XlsValidCategorySample> sut = new XlsxOrXlsCategoryValidator<>(XlsValidCategorySample.class, rows());

        assertThatCode(() -> sut.execute())
                .doesNotThrowAnyException();
    }

    private List<Row> rows() throws IOException {
        File xlsx = new File("src/test/java/io/github/kureung/xlsx_or_xls/xlsx_sample.xlsx");
        FileInputStream fileInputStream = new FileInputStream(xlsx);
        Workbook sheets = WorkbookFactory.create(fileInputStream);
        List<Row> result = new ArrayList<>();
        Sheet sheet = sheets.getSheetAt(0);
        for (Row each : sheet) {
            result.add(each);
        }
        fileInputStream.close();
        return result;
    }

    private static class XlsValidCategorySample {
        @CellExtract(categoryIndex = "B3", categoryValue = "성명", firstDataIndex = "B4")
        private String name;
    }
}
