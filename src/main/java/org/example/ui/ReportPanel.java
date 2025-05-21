package org.example.ui;

import org.example.dao.DepartmentDao;
import org.example.dao.EmployeeDao;
import org.example.model.Department;
import org.example.model.Employee;
import org.example.procedure.DepartmentEmployeesProcedure;
import org.example.procedure.EmployeeInfoProcedure;
import org.example.report.EmployeesByDepartmentReport;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ReportPanel extends JPanel {

    public ReportPanel() {
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        // Панель для кнопок отчетов
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Кнопка для отчета по сотрудникам по отделам
        JButton employeesByDeptButton = new JButton("Список сотрудников по отделам");
        employeesByDeptButton.addActionListener(e -> generateEmployeesByDepartmentReport());

        // Кнопка для вызова хранимой процедуры по сотрудникам отдела
        JButton departmentEmployeesButton = new JButton("Просмотр сотрудников отдела");
        departmentEmployeesButton.addActionListener(e -> showDepartmentEmployees());

        // Кнопка для вызова хранимой процедуры по информации о сотруднике
        JButton employeeInfoButton = new JButton("Информация о сотруднике");
        employeeInfoButton.addActionListener(e -> showEmployeeInfo());

        buttonPanel.add(employeesByDeptButton);
        buttonPanel.add(departmentEmployeesButton);
        buttonPanel.add(employeeInfoButton);

        // Добавляем панель кнопок вверху
        add(buttonPanel, BorderLayout.NORTH);

        // Добавляем панель описания в центр
        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setLayout(new BorderLayout());

        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setText("Выберите отчет для генерации из списка доступных отчетов выше.\n\n" +
                "\"Просмотр сотрудников отдела\" - вызывает хранимую процедуру GetDepartmentEmployees и отображает результат в таблице.");
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        descriptionPanel.add(scrollPane, BorderLayout.CENTER);

        add(descriptionPanel, BorderLayout.CENTER);
    }

    private void generateEmployeesByDepartmentReport() {
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            EmployeesByDepartmentReport.generateReport();
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    private void showDepartmentEmployees() {
        try {
            // Получаем список отделов для выбора
            DepartmentDao departmentDao = new DepartmentDao();
            List<Department> departments = departmentDao.getAllDepartments();

            if (departments.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "В базе данных нет отделов",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Создаем массив для выпадающего списка
            ComboItem[] departmentItems = new ComboItem[departments.size()];
            for (int i = 0; i < departments.size(); i++) {
                Department dept = departments.get(i);
                departmentItems[i] = new ComboItem(dept.getId(), dept.getName());
            }

            // Создаем выпадающий список с отделами
            JComboBox<ComboItem> departmentComboBox = new JComboBox<>(departmentItems);

            // Показываем диалог выбора отдела
            int result = JOptionPane.showConfirmDialog(this,
                    new Object[]{"Выберите отдел:", departmentComboBox},
                    "Выбор отдела",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            // Если пользователь нажал OK
            if (result == JOptionPane.OK_OPTION) {
                ComboItem selectedDepartment = (ComboItem) departmentComboBox.getSelectedItem();
                if (selectedDepartment != null) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    try {
                        DepartmentEmployeesProcedure.showResults(selectedDepartment.getId());
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this,
                                "Ошибка при выполнении запроса: " + ex.getMessage(),
                                "Ошибка",
                                JOptionPane.ERROR_MESSAGE);
                    } finally {
                        setCursor(Cursor.getDefaultCursor());
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Ошибка при получении списка отделов: " + e.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showEmployeeInfo() {
        try {
            // Получаем список сотрудников для выбора
            EmployeeDao employeeDao = new EmployeeDao();
            List<Employee> employees = employeeDao.getAllEmployees();

            if (employees.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "В базе данных нет сотрудников",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Создаем массив для выпадающего списка
            ComboItem[] employeeItems = new ComboItem[employees.size()];
            for (int i = 0; i < employees.size(); i++) {
                Employee emp = employees.get(i);
                employeeItems[i] = new ComboItem(emp.getId(), emp.getFull_name());
            }

            // Создаем выпадающий список с сотрудниками
            JComboBox<ComboItem> employeeComboBox = new JComboBox<>(employeeItems);

            // Показываем диалог выбора сотрудника
            int result = JOptionPane.showConfirmDialog(this,
                    new Object[]{"Выберите сотрудника:", employeeComboBox},
                    "Выбор сотрудника",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            // Если пользователь нажал OK
            if (result == JOptionPane.OK_OPTION) {
                ComboItem selectedEmployee = (ComboItem) employeeComboBox.getSelectedItem();
                if (selectedEmployee != null) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    try {
                        EmployeeInfoProcedure.showResults(selectedEmployee.getId());
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this,
                                "Ошибка при выполнении запроса: " + ex.getMessage(),
                                "Ошибка",
                                JOptionPane.ERROR_MESSAGE);
                    } finally {
                        setCursor(Cursor.getDefaultCursor());
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Ошибка при получении списка сотрудников: " + e.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}

