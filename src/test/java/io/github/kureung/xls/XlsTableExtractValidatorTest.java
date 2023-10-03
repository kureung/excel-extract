package io.github.kureung.xls;

import io.github.kureung.common.TableExtract;
import org.apache.poi.ss.usermodel.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class XlsTableExtractValidatorTest {
    @Test
    void validTable_is_not_throw_exception() {
        File xlsx = new File("src/test/java/io/github/kureung/xls/xls_sample.xls");

        XlsTableExtractValidator<XlsValidSheetSample> sut = new XlsTableExtractValidator<>(xlsx, XlsValidSheetSample.class);

        Assertions.assertThatCode(() -> sut.validRows())
                .doesNotThrowAnyException();
    }

    private Sheet sheet() throws IOException {
        File xls = new File("src/test/java/io/github/kureung/xls/xls_sample.xls");
        Workbook sheets = WorkbookFactory.create(xls);
        return sheets.getSheetAt(0);
    }

    @TableExtract(
            sheetNumber = 0,
            sheetName="사원명부",
            verifySheetName = true,
            firstDataRowIndex = 3,
            terminateCondition= @TableExtract.Entry(columNumber = 1, isNull = true)
    )
    private static class XlsValidSheetSample {
    }
}
