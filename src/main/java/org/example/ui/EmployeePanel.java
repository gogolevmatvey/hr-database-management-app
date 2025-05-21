package org.example.ui;

import org.example.dao.EmployeeDao;
import org.example.model.Employee;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

public class EmployeePanel extends BaseTablePanel {

    private EmployeeDao employeeDao;

    public EmployeePanel() {
        employeeDao = new EmployeeDao();
//        super.initTable();
//        super.initButtons();
    }

    @Override
    protected String[] getColumnNames() {
        return new String[]{"ФИО", "Телефон", "Образование"};
    }

    @Override
    protected void loadData() {
        tableModel.setRowCount(0);

        try {
            List<Employee> employees = employeeDao.getAllEmployees();
            for (Employee employee : employees) {
                Object[] row = {
                        //employee.getId(),
                        employee.getFull_name(),
                        employee.getPhone_number(),
                        employee.getEducation()
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            showError("Ошибка при загрузке данных: " + e.getMessage());
        }
    }

    @Override
    protected void addAction(ActionEvent e) {
        EmployeeDialog dialog = new EmployeeDialog(null);
        if (dialog.showDialog()) {
            try {
                employeeDao.addEmployee(dialog.getEmployee());
                loadData();
                showInfo("Сотрудник успешно добавлен");
            } catch (SQLException ex) {
                showError("Ошибка при добавлении сотрудника: " + ex.getMessage());
            }
        }
    }

    @Override
    protected void editAction(ActionEvent e) {
        int selectedRow = getSelectedRowId();
        if (selectedRow == -1) return;

        try {
            List<Employee> employees = employeeDao.getAllEmployees();
            if (selectedRow < employees.size()) {
                Employee employee = employees.get(selectedRow);
                EmployeeDialog dialog = new EmployeeDialog(employee);
                if (dialog.showDialog()) {
                    employeeDao.updateEmployee(dialog.getEmployee());
                    loadData();
                    showInfo("Сотрудник успешно обновлен");
                }
            }
        } catch (SQLException ex) {
            showError("Ошибка при редактировании сотрудника: " + ex.getMessage());
        }
    }

    @Override
    protected void deleteAction(ActionEvent e) {
        int selectedRow = getSelectedRowId();
        if (selectedRow == -1) return;

        try {
            List<Employee> employees = employeeDao.getAllEmployees();
            if (selectedRow < employees.size()) {
                int id = employees.get(selectedRow).getId();

                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Вы уверены, что хотите удалить этого сотрудника?",
                        "Подтверждение удаления",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        employeeDao.deleteEmployee(id);
                        loadData();
                        showInfo("Сотрудник успешно удален");
                    } catch (SQLException ex) {
                        showError("Ошибка при удалении сотрудника: " + ex.getMessage());
                    }
                }
            }
        } catch (SQLException ex) {
            showError("Ошибка при получении данных сотрудника: " + ex.getMessage());
        }
    }

    // Диалоговое окно для добавления/редактирования сотрудника
    private class EmployeeDialog extends JDialog {
        private JTextField fullNameField;
        private JTextField phoneField;
        private JTextField educationField;
        private JButton saveButton;
        private JButton cancelButton;
        private Employee employee;
        private boolean saved = false;

        public EmployeeDialog(Employee employee) {
            setTitle(employee == null ? "Добавление сотрудника" : "Редактирование сотрудника");
            setSize(400, 200);
            setModal(true);
            setLocationRelativeTo(null);
            setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

            this.employee = employee == null ? new Employee() : employee;

            initComponents();
        }

        private void initComponents() {
            JPanel formPanel = new JPanel(new SpringLayout());

            // ФИО
            JLabel fullNameLabel = new JLabel("ФИО:", JLabel.TRAILING);
            formPanel.add(fullNameLabel);
            fullNameField = new JTextField(20);
            fullNameField.setText(employee.getFull_name());
            fullNameLabel.setLabelFor(fullNameField);
            formPanel.add(fullNameField);

            // Телефон
            JLabel phoneLabel = new JLabel("Телефон:", JLabel.TRAILING);
            formPanel.add(phoneLabel);
            phoneField = new JTextField(20);
            if (employee.getId() != 0) {
                phoneField.setText(String.valueOf(employee.getPhone_number()));
            }
            phoneLabel.setLabelFor(phoneField);
            formPanel.add(phoneField);

            // Образование
            JLabel educationLabel = new JLabel("Образование:", JLabel.TRAILING);
            formPanel.add(educationLabel);
            educationField = new JTextField(20);
            educationField.setText(employee.getEducation());
            educationLabel.setLabelFor(educationField);
            formPanel.add(educationField);

            // Настройка сетки для SpringLayout
            SpringUtilities.makeCompactGrid(formPanel, 3, 2, 6, 6, 6, 6);

            // Кнопки
            JPanel buttonPanel = new JPanel();
            saveButton = new JButton("Сохранить");
            cancelButton = new JButton("Отмена");

            saveButton.addActionListener(e -> {
                if (validateInput()) {
                    employee.setFull_name(fullNameField.getText());
                    employee.setPhone_number(Long.parseLong(phoneField.getText()));
                    employee.setEducation(educationField.getText());
                    saved = true;
                    dispose();
                }
            });

            cancelButton.addActionListener(e -> dispose());

            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);

            add(formPanel);
            add(buttonPanel);
        }

        private boolean validateInput() {
            if (fullNameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "ФИО не может быть пустым", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            try {
                Long.parseLong(phoneField.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Телефон должен быть числом", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            return true;
        }

        public boolean showDialog() {
            setVisible(true);
            return saved;
        }

        public Employee getEmployee() {
            return employee;
        }
    }
}
