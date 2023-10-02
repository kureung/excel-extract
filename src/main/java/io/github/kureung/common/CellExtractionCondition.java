package io.github.kureung.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CellExtractionCondition {
    String cellCategoryIndexName() default "";
    String cellCategoryValue() default "";
    String firstExtractedCellIndexName();
    boolean verifyCellCategory() default true;
}
