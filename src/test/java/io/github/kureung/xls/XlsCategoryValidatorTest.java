package io.github.kureung.xls;

import io.github.kureung.common.CellExtract;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class XlsCategoryValidatorTest {
    @Test
    void name() throws IOException {
        XlsCategoryValidator<XlsValidCategorySample> sut = new XlsCategoryValidator<>(XlsValidCategorySample.class, rows());

        sut.execute();
    }

    private List<Row> rows() throws IOException {
        File xls = new File("src/test/java/io/github/kureung/xls/xls_sample.xls");
        Workbook sheets = WorkbookFactory.create(xls);
        List<Row> result = new ArrayList<>();
        Sheet sheet = sheets.getSheetAt(0);
        for (Row each : sheet) {
            result.add(each);
        }
        return result;
    }

    private static class XlsValidCategorySample {
        @CellExtract(categoryIndex = "B3", categoryValue = "성명", firstDataIndex = "B4")
        private String name;
    }
}
