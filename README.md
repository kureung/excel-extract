﻿# excel-extract

```java
    @TableExtract(
            sheetNumber = 0,
            sheetName="사원명부",
            verifySheetName = true,
            firstCategoryRowIndex = 3,
            terminateCondition= @TableExtract.Entry(columNumber = 1, isNull = true)
    )
    private static class XlsxSampleData {
        @CellExtract(categoryIndex = "B3", categoryValue = "성명", firstDataIndex = "B4")
        private String name;
        @CellExtract(categoryIndex = "C3", categoryValue = "나이", firstDataIndex = "C4")
        private String age;

        public XlsxSampleData() {
        }
    }
```
