package io.github.kureung.xlsx;

import io.github.kureung.common.TableExtract;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

class XlsxTableExtractValidatorTest {
    @Test
    void validSheetName_is_not_throw_exception() {
        File xlsx = new File("src/test/java/io/github/kureung/xlsx/xlsx_sample.xlsx");

        XlsxTableExtractValidator<XlsxValidSheetSample> sut = new XlsxTableExtractValidator<>(xlsx, XlsxValidSheetSample.class);

        Assertions.assertThatCode(() -> sut.validSheet())
                .doesNotThrowAnyException();
    }

    @TableExtract(sheetNumber = 0, sheetName = "사원명부")
    private static class XlsxValidSheetSample {
    }
}
