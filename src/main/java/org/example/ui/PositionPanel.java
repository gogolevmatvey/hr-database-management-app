package org.example.ui;

import org.example.dao.PositionDao;
import org.example.model.Position;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

public class PositionPanel extends BaseTablePanel {
    private PositionDao positionDao;

    public PositionPanel() {
        positionDao = new PositionDao();
    }

    @Override
    protected String[] getColumnNames() {
        return new String[]{"Название", "Описание", "Оплата за день"};
    }

    @Override
    protected void loadData() {
        tableModel.setRowCount(0);

        try {
            List<Position> positions = positionDao.getAllPositions();
            for (Position position : positions) {
                Object[] row = {
                        //position.getId(),
                        position.getName(),
                        position.getDescription(),
                        position.getDay_payment()
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            showError("Ошибка при загрузке данных: " + e.getMessage());
        }
    }

    @Override
    protected void addAction(ActionEvent e) {
        PositionDialog dialog = new PositionDialog(null);
        if (dialog.showDialog()) {
            try {
                positionDao.addPosition(dialog.getPosition());
                loadData();
                showInfo("Должность успешно добавлена");
            } catch (SQLException ex) {
                showError("Ошибка при добавлении должности: " + ex.getMessage());
            }
        }
    }

    @Override
    protected void editAction(ActionEvent e) {
        int selectedRow = getSelectedRowId();
        if (selectedRow == -1) return;

        try {
            List<Position> positions = positionDao.getAllPositions();
            if (selectedRow < positions.size()) {
                Position position = positions.get(selectedRow);
                PositionDialog dialog = new PositionDialog(position);
                if (dialog.showDialog()) {
                    positionDao.updatePosition(dialog.getPosition());
                    loadData();
                    showInfo("Должность успешно обновлена");
                }
            }
        } catch (SQLException ex) {
            showError("Ошибка при редактировании должности: " + ex.getMessage());
        }
    }

    @Override
    protected void deleteAction(ActionEvent e) {
        int selectedRow = getSelectedRowId();
        if (selectedRow == -1) return;

        try {
            List<Position> positions = positionDao.getAllPositions();
            if (selectedRow < positions.size()) {
                long id = positions.get(selectedRow).getId();

                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Вы уверены, что хотите удалить эту должность?",
                        "Подтверждение удаления",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        positionDao.deletePosition(id);
                        loadData();
                        showInfo("Должность успешно удалена");
                    } catch (SQLException ex) {
                        showError("Ошибка при удалении должности: " + ex.getMessage());
                    }
                }
            }
        } catch (SQLException ex) {
            showError("Ошибка при получении данных должности: " + ex.getMessage());
        }
    }

    // Диалоговое окно для добавления/редактирования должности
    private class PositionDialog extends JDialog {
        private JTextField nameField;
        private JTextField descriptionField;
        private JTextField dayPaymentField;
        private JButton saveButton;
        private JButton cancelButton;
        private Position position;
        private boolean saved = false;

        public PositionDialog(Position position) {
            setTitle(position == null ? "Добавление должности" : "Редактирование должности");
            setSize(400, 200);
            setModal(true);
            setLocationRelativeTo(null);
            setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

            this.position = position == null ? new Position() : position;

            initComponents();
        }

        private void initComponents() {
            JPanel formPanel = new JPanel(new SpringLayout());

            // Название должности
            JLabel nameLabel = new JLabel("Название:", JLabel.TRAILING);
            formPanel.add(nameLabel);
            nameField = new JTextField(20);
            nameField.setText(position.getName());
            nameLabel.setLabelFor(nameField);
            formPanel.add(nameField);

            // Описание
            JLabel descriptionLabel = new JLabel("Описание:", JLabel.TRAILING);
            formPanel.add(descriptionLabel);
            descriptionField = new JTextField(20);
            descriptionField.setText(position.getDescription());
            descriptionLabel.setLabelFor(descriptionField);
            formPanel.add(descriptionField);

            // Оплата за день
            JLabel dayPaymentLabel = new JLabel("Оплата за день:", JLabel.TRAILING);
            formPanel.add(dayPaymentLabel);
            dayPaymentField = new JTextField(20);
            if (position.getId() != 0) {
                dayPaymentField.setText(String.valueOf(position.getDay_payment()));
            }
            dayPaymentLabel.setLabelFor(dayPaymentField);
            formPanel.add(dayPaymentField);

            // Настройка сетки для SpringLayout
            SpringUtilities.makeCompactGrid(formPanel, 3, 2, 6, 6, 6, 6);

            // Кнопки
            JPanel buttonPanel = new JPanel();
            saveButton = new JButton("Сохранить");
            cancelButton = new JButton("Отмена");

            saveButton.addActionListener(e -> {
                if (validateInput()) {
                    position.setName(nameField.getText());
                    position.setDescription(descriptionField.getText());
                    position.setDay_payment(Integer.parseInt(dayPaymentField.getText()));
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
                JOptionPane.showMessageDialog(this, "Название должности не может быть пустым", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            try {
                Integer.parseInt(dayPaymentField.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Оплата за день должна быть числом", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            return true;
        }

        public boolean showDialog() {
            setVisible(true);
            return saved;
        }

        public Position getPosition() {
            return position;
        }
    }
}
