package org.example.diplommain;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.FileOutputStream;

public class ExcelTableCreator {
    public static void writeMatrixToExcel(double[][] matrix, String filePath, String sheetName) throws IOException {
        Workbook workbook;
        FileInputStream fis = null;

        try {
            // Открываем существующий файл, если он существует
            fis = new FileInputStream(filePath);
            workbook = WorkbookFactory.create(fis);
        } catch (IOException e) {
            // Если файл не существует, создаем новый Workbook
            workbook = WorkbookFactory.create(true);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }

        // Проверка существования листа
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet != null) {
            // Удаление существующего листа
            int sheetIndex = workbook.getSheetIndex(sheet);
            workbook.removeSheetAt(sheetIndex);
        }

        // Создание нового листа
        sheet = workbook.createSheet(sheetName);

        // Запись данных в ячейки
        for (int rowIndex = 0; rowIndex < matrix.length; rowIndex++) {
            Row row = sheet.createRow(rowIndex);
            for (int colIndex = 0; colIndex < matrix[rowIndex].length; colIndex++) {
                Cell cell = row.createCell(colIndex);
                cell.setCellValue(matrix[rowIndex][colIndex]);
            }
        }

        // Сохранение Workbook в файловую систему
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }

        workbook.close();
    }
}
