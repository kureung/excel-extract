package io.github.kureung.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableExtract {
    String firstDataIndex();
    String lastDateIndex();
    int sheetNumber() default 0;
    String sheetName() default "";
    boolean verifySheetName() default true;
}
