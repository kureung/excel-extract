package io.github.kureung.xlsx;

import io.github.kureung.common.CellExtract;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class XlsxCategoryValidatorTest {
    @Test
    void validCategory_is_not_throw_exception() throws IOException {
        XlsxCategoryValidator<XlsxValidCategorySample> sut = new XlsxCategoryValidator<>(XlsxValidCategorySample.class, rows());

        assertThatCode(() -> sut.execute())
                .doesNotThrowAnyException();
    }

    List<Row> rows() throws IOException {
        File xlsx = new File("src/test/java/io/github/kureung/xlsx/xlsx_sample.xlsx");
        return new ReadableWorkbook(xlsx)
                .getSheet(0).orElseThrow(() -> new IllegalArgumentException("not found sheet"))
                .read();
    }

    private static class XlsxValidCategorySample {
        @CellExtract(categoryIndex = "B3", categoryValue = "성명", firstDataIndex = "B4")
        private String name;
    }

    @Test
    void invalidCategory_is_throw_exception() throws IOException {
        XlsxCategoryValidator<XlsxInvalidCategorySample> sut = new XlsxCategoryValidator<>(XlsxInvalidCategorySample.class, rows());
        assertThatThrownBy(() -> sut.execute())
                .isInstanceOf(IllegalStateException.class);
    }

    private static class XlsxInvalidCategorySample {
        @CellExtract(categoryIndex = "B3", categoryValue = "성명1", firstDataIndex = "B4")
        private String name;
    }
}
