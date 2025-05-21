package org.example.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelExporter {

    public static void exportToExcel(JTable table, String title) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Сохранить Excel файл");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setSelectedFile(new File(title + ".xlsx"));

        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            // Добавляем расширение .xlsx, если оно отсутствует
            if (!file.getName().toLowerCase().endsWith(".xlsx")) {
                file = new File(file.getAbsolutePath() + ".xlsx");
            }

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet(title);

                // Создаем строку заголовка
                Row headerRow = sheet.createRow(0);
                TableModel model = table.getModel();

                /// Создаем стиль ячейки заголовка
                CellStyle headerStyle = workbook.createCellStyle();
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);
                headerStyle.setAlignment(HorizontalAlignment.CENTER);

                // Заполняем ячейки заголовка
                for (int i = 0; i < model.getColumnCount(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(model.getColumnName(i));
                    cell.setCellStyle(headerStyle);
                }

                // Заполняем строки данными
                for (int i = 0; i < model.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        Cell cell = row.createCell(j);
                        Object value = model.getValueAt(i, j);
                        if (value != null) {
                            cell.setCellValue(value.toString());
                        }
                    }
                }

                // Автоматически настраиваем ширину столбцов
                for (int i = 0; i < model.getColumnCount(); i++) {
                    sheet.autoSizeColumn(i);
                }

                // Записываем в файл
                try (FileOutputStream outputStream = new FileOutputStream(file)) {
                    workbook.write(outputStream);
                }

                JOptionPane.showMessageDialog(null,
                        "Данные успешно экспортированы в " + file.getAbsolutePath(),
                        "Экспорт завершен",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                        "Ошибка при экспорте: " + e.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}

