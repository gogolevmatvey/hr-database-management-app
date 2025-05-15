package org.example.ui;

import org.example.dao.DepartmentDao;
import org.example.dao.EmployeeDao;
import org.example.dao.EmploymentContractDao;
import org.example.dao.PositionDao;
import org.example.model.Department;
import org.example.model.Employee;
import org.example.model.EmploymentContract;
import org.example.model.Position;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class EmploymentContractPanel extends BaseTablePanel {
    private EmploymentContractDao contractDao;
    private EmployeeDao employeeDao;
    private DepartmentDao departmentDao;
    private PositionDao positionDao;

    public EmploymentContractPanel() {
        contractDao = new EmploymentContractDao();
        employeeDao = new EmployeeDao();
        departmentDao = new DepartmentDao();
        positionDao = new PositionDao();
    }

    @Override
    protected String[] getColumnNames() {
        return new String[]{"ID", "Номер", "Тип", "Дата создания", "Дата заключения", "Сотрудник", "Отдел", "Должность"};
    }

    @Override
    protected void loadData() {
        tableModel.setRowCount(0);

        try {
            List<EmploymentContract> contracts = contractDao.getAllContracts();
            for (EmploymentContract contract : contracts) {
                // Получаем имена сотрудника, отдела и должности для отображения
                String employeeName = getEmployeeName(contract.getEmployee_id());
                String departmentName = getDepartmentName(contract.getDepartment_id());
                String positionName = getPositionName(contract.getPosition_id());

                Object[] row = {
                        contract.getId(),
                        contract.getNumber(),
                        contract.getType(),
                        contract.getCreation_date(),
                        contract.getDate_of_conclusion(),
                        employeeName,
                        departmentName,
                        positionName
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            showError("Ошибка при загрузке данных: " + e.getMessage());
        }
    }

    private String getEmployeeName(long employeeId) {
        try {
            Employee employee = employeeDao.getEmployeeById((int) employeeId);
            return employee != null ? employee.getFull_name() : "Неизвестно";
        } catch (SQLException e) {
            return "Ошибка";
        }
    }

    private String getDepartmentName(long departmentId) {
        try {
            Department department = departmentDao.getDepartmentById(departmentId);
            return department != null ? department.getName() : "Неизвестно";
        } catch (SQLException e) {
            return "Ошибка";
        }
    }

    private String getPositionName(long positionId) {
        try {
            Position position = positionDao.getPositionById(positionId);
            return position != null ? position.getName() : "Неизвестно";
        } catch (SQLException e) {
            return "Ошибка";
        }
    }

    @Override
    protected void addAction(ActionEvent e) {
        ContractDialog dialog = new ContractDialog(null);
        if (dialog.showDialog()) {
            try {
                contractDao.addContract(dialog.getContract());
                loadData();
                showInfo("Трудовой договор успешно добавлен");
            } catch (SQLException ex) {
                showError("Ошибка при добавлении трудового договора: " + ex.getMessage());
            }
        }
    }

    @Override
    protected void editAction(ActionEvent e) {
        int selectedRow = getSelectedRowId();
        if (selectedRow == -1) return;

        long id = (long) tableModel.getValueAt(selectedRow, 0);

        try {
            EmploymentContract contract = contractDao.getContractById(id);
            if (contract != null) {
                ContractDialog dialog = new ContractDialog(contract);
                if (dialog.showDialog()) {
                    contractDao.updateContract(dialog.getContract());
                    loadData();
                    showInfo("Трудовой договор успешно обновлен");
                }
            }
        } catch (SQLException ex) {
            showError("Ошибка при редактировании трудового договора: " + ex.getMessage());
        }
    }

    @Override
    protected void deleteAction(ActionEvent e) {
        int selectedRow = getSelectedRowId();
        if (selectedRow == -1) return;

        long id = (long) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Вы уверены, что хотите удалить этот трудовой договор?",
                "Подтверждение удаления",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                contractDao.deleteContract(id);
                loadData();
                showInfo("Трудовой договор успешно удален");
            } catch (SQLException ex) {
                showError("Ошибка при удалении трудового договора: " + ex.getMessage());
            }
        }
    }

    // Диалоговое окно для добавления/редактирования трудового договора
    private class ContractDialog extends JDialog {
        private JTextField numberField;
        private JTextField typeField;
        private JTextField creationDateField;
        private JTextField conclusionDateField;
        private JComboBox<ComboItem> employeeComboBox;
        private JComboBox<ComboItem> departmentComboBox;
        private JComboBox<ComboItem> positionComboBox;
        private JButton saveButton;
        private JButton cancelButton;
        private EmploymentContract contract;
        private boolean saved = false;

        public ContractDialog(EmploymentContract contract) {
            setTitle(contract == null ? "Добавление трудового договора" : "Редактирование трудового");
            setSize(500, 350);
            setModal(true);
            setLocationRelativeTo(null);
            setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

            this.contract = contract == null ? new EmploymentContract() : contract;

            initComponents();
        }

        private void initComponents() {
            JPanel formPanel = new JPanel(new SpringLayout());

            // Номер договора
            JLabel numberLabel = new JLabel("Номер договора:", JLabel.TRAILING);
            formPanel.add(numberLabel);
            numberField = new JTextField(10);
            if (contract.getId() != 0) {
                numberField.setText(String.valueOf(contract.getNumber()));
            }
            numberLabel.setLabelFor(numberField);
            formPanel.add(numberField);

            // Тип договора
            JLabel typeLabel = new JLabel("Тип договора:", JLabel.TRAILING);
            formPanel.add(typeLabel);
            typeField = new JTextField(20);
            typeField.setText(contract.getType());
            typeLabel.setLabelFor(typeField);
            formPanel.add(typeField);

            // Дата создания
            JLabel creationDateLabel = new JLabel("Дата создания (ГГГГ-ММ-ДД):", JLabel.TRAILING);
            formPanel.add(creationDateLabel);
            creationDateField = new JTextField(10);
            if (contract.getId() != 0) {
                creationDateField.setText(contract.getCreation_date().toString());
            } else {
                creationDateField.setText(LocalDate.now().toString());
            }
            creationDateLabel.setLabelFor(creationDateField);
            formPanel.add(creationDateField);

            // Дата заключения
            JLabel conclusionDateLabel = new JLabel("Дата заключения:", JLabel.TRAILING);
            formPanel.add(conclusionDateLabel);
            conclusionDateField = new JTextField(10);
            conclusionDateField.setText(contract.getDate_of_conclusion());
            conclusionDateLabel.setLabelFor(conclusionDateField);
            formPanel.add(conclusionDateField);

            // Сотрудник
            JLabel employeeLabel = new JLabel("Сотрудник:", JLabel.TRAILING);
            formPanel.add(employeeLabel);
            employeeComboBox = new JComboBox<>();
            loadEmployees();
            if (contract.getId() != 0) {
                selectEmployee(contract.getEmployee_id());
            }
            employeeLabel.setLabelFor(employeeComboBox);
            formPanel.add(employeeComboBox);

            // Отдел
            JLabel departmentLabel = new JLabel("Отдел:", JLabel.TRAILING);
            formPanel.add(departmentLabel);
            departmentComboBox = new JComboBox<>();
            loadDepartments();
            if (contract.getId() != 0) {
                selectDepartment(contract.getDepartment_id());
            }
            departmentLabel.setLabelFor(departmentComboBox);
            formPanel.add(departmentComboBox);

            // Должность
            JLabel positionLabel = new JLabel("Должность:", JLabel.TRAILING);
            formPanel.add(positionLabel);
            positionComboBox = new JComboBox<>();
            loadPositions();
            if (contract.getId() != 0) {
                selectPosition(contract.getPosition_id());
            }
            positionLabel.setLabelFor(positionComboBox);
            formPanel.add(positionComboBox);

            // Настройка сетки для SpringLayout
            SpringUtilities.makeCompactGrid(formPanel, 7, 2, 6, 6, 6, 6);

            // Кнопки
            JPanel buttonPanel = new JPanel();
            saveButton = new JButton("Сохранить");
            cancelButton = new JButton("Отмена");

            saveButton.addActionListener(e -> {
                if (validateInput()) {
                    contract.setNumber(Integer.parseInt(numberField.getText()));
                    contract.setType(typeField.getText());
                    contract.setCreation_date(LocalDate.parse(creationDateField.getText()));
                    contract.setDate_of_conclusion(conclusionDateField.getText());

                    ComboItem selectedEmployee = (ComboItem) employeeComboBox.getSelectedItem();
                    contract.setEmployee_id(selectedEmployee.getId());

                    ComboItem selectedDepartment = (ComboItem) departmentComboBox.getSelectedItem();
                    contract.setDepartment_id(selectedDepartment.getId());

                    ComboItem selectedPosition = (ComboItem) positionComboBox.getSelectedItem();
                    contract.setPosition_id(selectedPosition.getId());

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

        private void loadEmployees() {
            try {
                List<Employee> employees = employeeDao.getAllEmployees();
                for (Employee employee : employees) {
                    employeeComboBox.addItem(new ComboItem(employee.getId(), employee.getFull_name()));
                }
            } catch (SQLException e) {
                showError("Ошибка при загрузке списка сотрудников: " + e.getMessage());
            }
        }

        private void selectEmployee(long employeeId) {
            for (int i = 0; i < employeeComboBox.getItemCount(); i++) {
                ComboItem item = employeeComboBox.getItemAt(i);
                if (item.getId() == employeeId) {
                    employeeComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }

        private void loadDepartments() {
            try {
                List<Department> departments = departmentDao.getAllDepartments();
                for (Department department : departments) {
                    departmentComboBox.addItem(new ComboItem(department.getId(), department.getName()));
                }
            } catch (SQLException e) {
                showError("Ошибка при загрузке списка отделов: " + e.getMessage());
            }
        }

        private void selectDepartment(long departmentId) {
            for (int i = 0; i < departmentComboBox.getItemCount(); i++) {
                ComboItem item = departmentComboBox.getItemAt(i);
                if (item.getId() == departmentId) {
                    departmentComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }

        private void loadPositions() {
            try {
                List<Position> positions = positionDao.getAllPositions();
                for (Position position : positions) {
                    positionComboBox.addItem(new ComboItem(position.getId(), position.getName()));
                }
            } catch (SQLException e) {
                showError("Ошибка при загрузке списка должностей: " + e.getMessage());
            }
        }

        private void selectPosition(long positionId) {
            for (int i = 0; i < positionComboBox.getItemCount(); i++) {
                ComboItem item = positionComboBox.getItemAt(i);
                if (item.getId() == positionId) {
                    positionComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }

        private boolean validateInput() {
            try {
                Integer.parseInt(numberField.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Номер договора должен быть числом", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (typeField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Тип договора не может быть пустым", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            try {
                LocalDate.parse(creationDateField.getText());
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Дата создания должна быть в формате ГГГГ-ММ-ДД", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (conclusionDateField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Дата заключения не может быть пустой", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (employeeComboBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Необходимо выбрать сотрудника", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (departmentComboBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Необходимо выбрать отдел", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (positionComboBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Необходимо выбрать должность", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            return true;
        }

        public boolean showDialog() {
            setVisible(true);
            return saved;
        }

        public EmploymentContract getContract() {
            return contract;
        }
    }
}

