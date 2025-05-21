package org.example.ui;

import org.example.dao.DepartmentDao;
import org.example.model.Department;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

public class DepartmentPanel extends BaseTablePanel{
    private DepartmentDao departmentDao;

    public DepartmentPanel() {
        departmentDao = new DepartmentDao();
    }

    @Override
    protected String[] getColumnNames() {
        return new String[]{"Название отдела"};
    }

    @Override
    protected void loadData() {
        tableModel.setRowCount(0);

        try {
            List<Department> departments = departmentDao.getAllDepartments();
            for (Department department : departments) {
                Object[] row = {
                        //department.getId(),
                        department.getName()
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            showError("Ошибка при загрузке данных: " + e.getMessage());
        }
    }

    @Override
    protected void addAction(ActionEvent e) {
        DepartmentDialog dialog = new DepartmentDialog(null);
        if (dialog.showDialog()) {
            try {
                departmentDao.addDepartment(dialog.getDepartment());
                loadData();
                showInfo("Отдел успешно добавлен");
            } catch (SQLException ex) {
                showError("Ошибка при добавлении отдела: " + ex.getMessage());
            }
        }
    }

    @Override
    protected void editAction(ActionEvent e) {
        int selectedRow = getSelectedRowId();
        if (selectedRow == -1) return;

        try {
            // Получаем список всех отделов
            List<Department> departments = departmentDao.getAllDepartments();
            if (selectedRow < departments.size()) {
                Department department = departments.get(selectedRow);
                DepartmentDialog dialog = new DepartmentDialog(department);
                if (dialog.showDialog()) {
                    departmentDao.updateDepartment(dialog.getDepartment());
                    loadData();
                    showInfo("Отдел успешно обновлен");
                }
            }
        } catch (SQLException ex) {
            showError("Ошибка при редактировании отдела: " + ex.getMessage());
        }
    }

    @Override
    protected void deleteAction(ActionEvent e) {
        int selectedRow = getSelectedRowId();
        if (selectedRow == -1) return;

        try {
            // Получаем список всех отделов
            List<Department> departments = departmentDao.getAllDepartments();
            if (selectedRow < departments.size()) {
                long id = departments.get(selectedRow).getId();

                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Вы уверены, что хотите удалить этот отдел?",
                        "Подтверждение удаления",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        departmentDao.deleteDepartment(id);
                        loadData();
                        showInfo("Отдел успешно удален");
                    } catch (SQLException ex) {
                        showError("Ошибка при удалении отдела: " + ex.getMessage());
                    }
                }
            }
        } catch (SQLException ex) {
            showError("Ошибка при получении данных отдела: " + ex.getMessage());
        }
    }

    // Диалоговое окно для добавления/редактирования отдела
    private class DepartmentDialog extends JDialog {
        private JTextField nameField;
        private JButton saveButton;
        private JButton cancelButton;
        private Department department;
        private boolean saved = false;

        public DepartmentDialog(Department department) {
            setTitle(department == null ? "Добавление отдела" : "Редактирование отдела");
            setSize(400, 150);
            setModal(true);
            setLocationRelativeTo(null);
            setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

            this.department = department == null ? new Department() : department;

            initComponents();
        }

        private void initComponents() {
            JPanel formPanel = new JPanel(new SpringLayout());

            // Название отдела
            JLabel nameLabel = new JLabel("Название:", JLabel.TRAILING);
            formPanel.add(nameLabel);
            nameField = new JTextField(20);
            nameField.setText(department.getName());
            nameLabel.setLabelFor(nameField);
            formPanel.add(nameField);

            // Настройка сетки для SpringLayout
            SpringUtilities.makeCompactGrid(formPanel, 1, 2, 6, 6, 6, 6);

            // Кнопки
            JPanel buttonPanel = new JPanel();
            saveButton = new JButton("Сохранить");
            cancelButton = new JButton("Отмена");

            saveButton.addActionListener(e -> {
                if (validateInput()) {
                    department.setName(nameField.getText());
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
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Название отдела не может быть пустым", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        }

        public boolean showDialog() {
            setVisible(true);
            return saved;
        }

        public Department getDepartment() {
            return department;
        }
    }
}
