package io.github.kureung.csv;

import io.github.kureung.common.TableExtract;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThatCode;

class CsvTableExtractValidatorTest {
    @Test
    void validTable_is_not_throw_exception() {
        File xlsx = new File("src/test/java/io/github/kureung/xls/xls_sample.xls");

        CsvTableExtractValidator<CsvValidSheetSample> sut = new CsvTableExtractValidator<>(xlsx, CsvValidSheetSample.class);

        assertThatCode(() -> sut.validRows())
                .doesNotThrowAnyException();
    }

    @TableExtract(
            sheetNumber = 0,
            sheetName="사원명부",
            verifySheetName = true,
            firstDataRowIndex = 1,
            terminateCondition= @TableExtract.Entry(columNumber = 1, isNull = true)
    )
    private static class CsvValidSheetSample {
    }
}
