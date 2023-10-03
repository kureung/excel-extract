package io.github.kureung.xls;

import io.github.kureung.common.CellExtract;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class XlsCategoryValidatorTest {
    @Test
    void name() throws IOException {
        XlsCategoryValidator<XlsValidCategorySample> sut = new XlsCategoryValidator<>(XlsValidCategorySample.class, sheet());

        sut.execute();
    }

    private Sheet sheet() throws IOException {
        File xls = new File("src/test/java/io/github/kureung/xls/xls_sample.xls");
        Workbook sheets = WorkbookFactory.create(xls);
        return sheets.getSheetAt(0);
    }

    private static class XlsValidCategorySample {
        @CellExtract(categoryIndex = "B3", categoryValue = "성명", firstDataIndex = "B4")
        private String name;
    }
}
