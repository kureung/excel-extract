package io.github.kureung.xlsx_or_xls;

import io.github.kureung.common.TableExtract;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

class XlsxOrXlsTableExtractValidatorTest {
    @Test
    void validSheetName_is_not_throw_exception() {
        File xlsx = new File("src/test/java/io/github/kureung/xlsx_or_xls/xlsx_sample.xlsx");

        XlsxOrXlsTableExtractValidator<XlsxValidSheetSample> sut = new XlsxOrXlsTableExtractValidator<>(xlsx, XlsxValidSheetSample.class);

        Assertions.assertThatCode(() -> sut.validRows())
                .doesNotThrowAnyException();
    }

    @TableExtract(sheetNumber = 0, sheetName = "사원명부")
    private static class XlsxValidSheetSample {
    }
}
