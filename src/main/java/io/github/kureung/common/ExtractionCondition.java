package io.github.kureung.common;

import java.util.List;

public class ExtractionCondition<T> {
    private T instanceCreatedWithNoArgument;
    private int sheetPage;
    private String sheetName;
    private List<CellInformation> verificationInventory;
    private CellIndexName startColumnCellIndexName;
    private CellIndexName endColumnCellIndexName;
    private ExtractionTerminationCondition extractionTerminationCondition;

    private ExtractionCondition(
            T instanceCreatedWithNoArgument,
            int sheetPage,
            String sheetName,
            List<CellInformation> verificationInventory,
            CellIndexName startColumnCellIndexName,
            CellIndexName endColumnCellIndexName,
            ExtractionTerminationCondition extractionTerminationCondition
    ) {
        this.instanceCreatedWithNoArgument = instanceCreatedWithNoArgument;
        this.sheetPage = sheetPage;
        this.sheetName = sheetName;
        this.verificationInventory = verificationInventory;
        this.startColumnCellIndexName = startColumnCellIndexName;
        this.endColumnCellIndexName = endColumnCellIndexName;
        this.extractionTerminationCondition = extractionTerminationCondition;
    }

    public static <T> ExtractionConditionBuilder<T> builder(Class<T> clazz) {
        return new ExtractionConditionBuilder<>();
    }

    public T instanceCreatedWithNoArgument() {
        return instanceCreatedWithNoArgument;
    }

    public int sheetPage() {
        return sheetPage;
    }

    public String sheetName() {
        return sheetName;
    }

    public List<CellInformation> verificationInventory() {
        return verificationInventory;
    }

    public int startColumnCellXCoordinate() {
        return startColumnCellIndexName.xCoordinate();
    }

    public int startColumnCellYCoordinate() {
        return startColumnCellIndexName.yCoordinate();
    }

    public int endColumnCellXCoordinate() {
        return endColumnCellIndexName.xCoordinate();
    }

    public int endColumnCellYCoordinate() {
        return endColumnCellIndexName.yCoordinate();
    }

    public int extractionTerminationConditionColumnIndex() {
        return extractionTerminationCondition.columnIndex();
    }

    public String extractionTerminationConditionCellValue() {
        return extractionTerminationCondition.value();
    }

    public static class ExtractionConditionBuilder<T> {
        private T instanceCreatedWithNoArgument;
        private int sheetPage;
        private String sheetName;
        private List<CellInformation> verificationInventory;
        private CellIndexName startColumnCellIndexName;
        private CellIndexName endColumnCellIndexName;
        private ExtractionTerminationCondition extractionTerminationCondition;

        public ExtractionConditionBuilder<T> instanceCreatedWithNoArgument(T instanceCreatedWithNoArgument) {
            this.instanceCreatedWithNoArgument = instanceCreatedWithNoArgument;
            return this;
        }

        public ExtractionConditionBuilder<T> sheetInformation(int sheetPage, String sheetName) {
            this.sheetPage = sheetPage;
            this.sheetName = sheetName;
            return this;
        }

        public ExtractionConditionBuilder<T> addVerificationConditionCell(String cellIndexName, String cellValue) {
            verificationInventory.add(new CellInformation(cellIndexName, cellValue));
            return this;
        }

        public ExtractionConditionBuilder<T> extractionRangeCellIndexName(String startCellIndexName, String endCellIndexName) {
            this.startColumnCellIndexName = new CellIndexName(startCellIndexName);
            this.endColumnCellIndexName = new CellIndexName(endCellIndexName);
            return this;
        }

        public ExtractionConditionBuilder<T> terminationConditionCellInformation(int cellXCoordinate, String cellValue) {
            this.extractionTerminationCondition = new ExtractionTerminationCondition(cellXCoordinate, cellValue);
            return this;
        }

        public ExtractionCondition<T> build() {
            return new ExtractionCondition<>(
                    instanceCreatedWithNoArgument,
                    sheetPage,
                    sheetName,
                    verificationInventory,
                    startColumnCellIndexName,
                    endColumnCellIndexName,
                    extractionTerminationCondition
            );
        }
    }
}
