package org.example.procedure;

import org.example.dao.DepartmentDao;
import org.example.model.Department;
import org.example.db.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class DepartmentEmployeesProcedure {

    public static void showResults(long departmentId) throws SQLException {
        // Получаем соединение с базой данных
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall("{call GetDepartmentEmployees(?)}")) {

            // Устанавливаем параметр
            stmt.setLong(1, departmentId);

            // Выполняем процедуру
            try (ResultSet rs = stmt.executeQuery()) {
                // Создаем модель таблицы для результатов
                DefaultTableModel model = createTableModelFromResultSet(rs);

                // Получаем название отдела
                DepartmentDao departmentDao = new DepartmentDao();
                Department department = departmentDao.getDepartmentById(departmentId);
                String departmentName = department != null ? department.getName() : "Отдел " + departmentId;

                // Создаем таблицу и окно для отображения результатов
                JTable resultTable = new JTable(model);
                resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                resultTable.getTableHeader().setReorderingAllowed(false);

                JScrollPane scrollPane = new JScrollPane(resultTable);

                JFrame resultFrame = new JFrame("Сотрудники отдела: " + departmentName);
                resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                resultFrame.setSize(800, 400);
                resultFrame.setLocationRelativeTo(null);
                resultFrame.add(scrollPane);
                resultFrame.setVisible(true);

                // Если нет данных, показываем сообщение
                if (model.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(resultFrame,
                            "В выбранном отделе нет сотрудников",
                            "Пустой результат",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка при выполнении запроса: " + e.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            throw e;
        }
    }

    /**
     * Создает модель таблицы из результата запроса, пропуская столбцы с ID
     * @param rs Результат запроса
     * @return Модель таблицы с данными
     */
    private static DefaultTableModel createTableModelFromResultSet(ResultSet rs) throws SQLException {
        // Получаем метаданные результата
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Создаем заголовки столбцов и запоминаем индексы столбцов с ID
        // Vector используется из-за DefaultTableModel
        Vector<String> columnNames = new Vector<>();
        Vector<Integer> columnIndices = new Vector<>();

        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnLabel(i);
            // Пропускаем столбцы, содержащие ID
            if (!columnName.contains("ID") && !columnName.endsWith("Id")) {
                columnNames.add(columnName);
                columnIndices.add(i);
            }
        }

        // Создаем данные
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int i = 0; i < columnIndices.size(); i++) {
                row.add(rs.getObject(columnIndices.get(i)));
            }
            data.add(row);
        }

        // Создаем и возвращаем модель таблицы
        return new DefaultTableModel(data, columnNames);
    }
}


