package org.example.ui;

import org.example.dao.DocumentDao;
import org.example.dao.EmployeeDao;
import org.example.model.Document;
import org.example.model.Employee;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class DocumentPanel extends BaseTablePanel {
    private DocumentDao documentDao;
    private EmployeeDao employeeDao;

    public DocumentPanel() {
        documentDao = new DocumentDao();
        employeeDao = new EmployeeDao();
    }

    @Override
    protected String[] getColumnNames() {
        return new String[]{"ID", "Тип", "Серия", "Номер", "Дата", "Кем выдан", "ID сотрудника"};
    }

    @Override
    protected void loadData() {
        tableModel.setRowCount(0);

        try {
            List<Document> documents = documentDao.getAllDocuments();
            for (Document document : documents) {
                Object[] row = {
                        document.getId(),
                        document.getType(),
                        document.getSerial(),
                        document.getNumber(),
                        document.getDate(),
                        document.getGiven_by(),
                        document.getEmployee_id()
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            showError("Ошибка при загрузке данных: " + e.getMessage());
        }
    }

    @Override
    protected void addAction(ActionEvent e) {
        DocumentDialog dialog = new DocumentDialog(null);
        if (dialog.showDialog()) {
            try {
                documentDao.addDocument(dialog.getDocument());
                loadData();
                showInfo("Документ успешно добавлен");
            } catch (SQLException ex) {
                showError("Ошибка при добавлении документа: " + ex.getMessage());
            }
        }
    }

    @Override
    protected void editAction(ActionEvent e) {
        int selectedRow = getSelectedRowId();
        if (selectedRow == -1) return;

        long id = (long) tableModel.getValueAt(selectedRow, 0);

        try {
            Document document = documentDao.getDocumentById(id);
            if (document != null) {
                DocumentDialog dialog = new DocumentDialog(document);
                if (dialog.showDialog()) {
                    documentDao.updateDocument(dialog.getDocument());
                    loadData();
                    showInfo("Документ успешно обновлен");
                }
            }
        } catch (SQLException ex) {
            showError("Ошибка при редактировании документа: " + ex.getMessage());
        }
    }

    @Override
    protected void deleteAction(ActionEvent e) {
        int selectedRow = getSelectedRowId();
        if (selectedRow == -1) return;

        long id = (long) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Вы уверены, что хотите удалить этот документ?",
                "Подтверждение удаления",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                documentDao.deleteDocument(id);
                loadData();
                showInfo("Документ успешно удален");
            } catch (SQLException ex) {
                showError("Ошибка при удалении документа: " + ex.getMessage());
            }
        }
    }

    // Диалоговое окно для добавления/редактирования документа
    private class DocumentDialog extends JDialog {
        private JComboBox<Integer> typeComboBox;
        private JTextField serialField;
        private JTextField numberField;
        private JTextField dateField;
        private JTextField givenByField;
        private JComboBox<ComboItem> employeeComboBox;
        private JButton saveButton;
        private JButton cancelButton;
        private Document document;
        private boolean saved = false;

        public DocumentDialog(Document document) {
            setTitle(document == null ? "Добавление документа" : "Редактирование документа");
            setSize(400, 300);
            setModal(true);
            setLocationRelativeTo(null);
            setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

            this.document = document == null ? new Document() : document;

            initComponents();
        }

        private void initComponents() {
            JPanel formPanel = new JPanel(new SpringLayout());

            // Тип документа
            JLabel typeLabel = new JLabel("Тип документа:", JLabel.TRAILING);
            formPanel.add(typeLabel);
            Integer[] documentTypes = {1, 2, 3, 4}; // Предполагаемые типы документов
            typeComboBox = new JComboBox<>(documentTypes);
            if (document.getId() != 0) {
                typeComboBox.setSelectedItem(document.getType());
            }
            typeLabel.setLabelFor(typeComboBox);
            formPanel.add(typeComboBox);

            // Серия
            JLabel serialLabel = new JLabel("Серия:", JLabel.TRAILING);
            formPanel.add(serialLabel);
            serialField = new JTextField(10);
            if (document.getId() != 0) {
                serialField.setText(String.valueOf(document.getSerial()));
            }
            serialLabel.setLabelFor(serialField);
            formPanel.add(serialField);

            // Номер
            JLabel numberLabel = new JLabel("Номер:", JLabel.TRAILING);
            formPanel.add(numberLabel);
            numberField = new JTextField(10);
            if (document.getId() != 0) {
                numberField.setText(String.valueOf(document.getNumber()));
            }
            numberLabel.setLabelFor(numberField);
            formPanel.add(numberField);

            // Дата
            JLabel dateLabel = new JLabel("Дата (ГГГГ-ММ-ДД):", JLabel.TRAILING);
            formPanel.add(dateLabel);
            dateField = new JTextField(10);
            if (document.getId() != 0) {
                dateField.setText(document.getDate().toString());
            }
            dateLabel.setLabelFor(dateField);
            formPanel.add(dateField);

            // Кем выдан
            JLabel givenByLabel = new JLabel("Кем выдан:", JLabel.TRAILING);
            formPanel.add(givenByLabel);
            givenByField = new JTextField(20);
            givenByField.setText(document.getGiven_by());
            givenByLabel.setLabelFor(givenByField);
            formPanel.add(givenByField);

            // Сотрудник
            JLabel employeeLabel = new JLabel("Сотрудник:", JLabel.TRAILING);
            formPanel.add(employeeLabel);
            employeeComboBox = new JComboBox<>();
            loadEmployees();
            if (document.getId() != 0) {
                selectEmployee(document.getEmployee_id());
            }
            employeeLabel.setLabelFor(employeeComboBox);
            formPanel.add(employeeComboBox);

            // Настройка сетки для SpringLayout
            SpringUtilities.makeCompactGrid(formPanel, 6, 2, 6, 6, 6, 6);

            // Кнопки
            JPanel buttonPanel = new JPanel();
            saveButton = new JButton("Сохранить");
            cancelButton = new JButton("Отмена");

            saveButton.addActionListener(e -> {
                if (validateInput()) {
                    document.setType((Integer) typeComboBox.getSelectedItem());
                    document.setSerial(Integer.parseInt(serialField.getText()));
                    document.setNumber(Long.parseLong(numberField.getText()));
                    document.setDate(LocalDate.parse(dateField.getText()));
                    document.setGiven_by(givenByField.getText());
                    ComboItem selectedEmployee = (ComboItem) employeeComboBox.getSelectedItem();
                    document.setEmployee_id(selectedEmployee.getId());
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

        private boolean validateInput() {
            try {
                Integer.parseInt(serialField.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Серия должна быть числом", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            try {
                Long.parseLong(numberField.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Номер должен быть числом", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            try {
                LocalDate.parse(dateField.getText());
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Дата должна быть в формате ГГГГ-ММ-ДД", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (givenByField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Поле 'Кем выдан' не может быть пустым", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (employeeComboBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Необходимо выбрать сотрудника", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            return true;
        }

        public boolean showDialog() {
            setVisible(true);
            return saved;
        }

        public Document getDocument() {
            return document;
        }
    }
}
