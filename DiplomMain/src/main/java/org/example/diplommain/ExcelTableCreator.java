package org.example.diplommain;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.FileOutputStream;

public class ExcelTableCreator {
    public static void writeMatrixToExcel(double[][] matrix, String filePath, String sheetName) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = WorkbookFactory.create(fis);
        // Create a Sheet
        Sheet sheet = workbook.getSheet(sheetName);
        if(sheet == null){
            sheet = workbook.createSheet(sheetName);
        } else {
            workbook.removeSheetAt(workbook.getSheetIndex(sheet));
            sheet = workbook.createSheet(sheetName);
        }

        int startRow = 0;

        // Write data to cells
        for (int rowIndex = 0; rowIndex < matrix.length; rowIndex++) {
            Row row = sheet.createRow(startRow + rowIndex);
            for (int colIndex = 0; colIndex < matrix[rowIndex].length; colIndex++) {
                Cell cell = row.createCell(colIndex);
                cell.setCellValue(matrix[rowIndex][colIndex]);
            }
        }

        // Write the Workbook to the file system
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
        }

        // Close the Workbook
        workbook.close();
        fis.close();
    }
}
