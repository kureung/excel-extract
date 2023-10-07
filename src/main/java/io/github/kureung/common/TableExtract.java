package io.github.kureung.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableExtract {
    int sheetNumber() default 0;
    String sheetName() default "";
    int firstCategoryRowIndex() default 0;
    boolean verifySheetName() default true;
    Entry terminateCondition() default @Entry(columNumber=-1);

   @interface Entry {
       int columNumber();
       String value() default "";
       boolean isNull() default false;
   }
}
