package io.github.kureung.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class CellIndexNameTest {
    @Test
    @DisplayName("셀 좌표 이름은 알파벳뒤에 숫자가 봐야한다.")
    void valid_constructor_parameter() {
        assertThatCode(() -> new CellIndexName("A5"))
                .doesNotThrowAnyException();

    }

    @DisplayName("셀 좌표 이름은 알파벳뒤에 숫자 양식이 아닐경우 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(strings = {
            "A",
            "1",
            "1A"
    })
    void invalid_constructor_parameter(String cellIndexName) {
        assertThatThrownBy(() ->new CellIndexName(cellIndexName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("셀 좌표 이름은 '[A-Za-z]+[0-9]+' 양식이어야 합니다.");

    }

    @Test
    @DisplayName("x 좌표 조회")
    void convertToXCoordinate() {
        CellIndexName sut = new CellIndexName("A5");

        int result = sut.xCoordinate();

        assertThat(result).isEqualTo(0);
    }

    @Test
    @DisplayName("y 좌표 조회")
    void convertToYCoordinate() {
        CellIndexName sut = new CellIndexName("A5");

        int result = sut.yCoordinate();

        assertThat(result).isEqualTo(4);
    }
}


