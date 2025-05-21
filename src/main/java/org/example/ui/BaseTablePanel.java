package org.example.ui;

import org.example.util.ExcelExporter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;

public abstract class BaseTablePanel extends JPanel {
    protected JTable table;
    protected DefaultTableModel tableModel;
    protected JScrollPane scrollPane;
    protected JPanel buttonPanel;
    protected JButton addButton;
    protected JButton editButton;
    protected JButton deleteButton;
    protected JButton refreshButton;
    protected JButton exportButton;

    public BaseTablePanel() {
        setLayout(new BorderLayout());

        initTable();
        initButtons();

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

//        loadData();
        // асинхронная загрузка
        SwingUtilities.invokeLater(this::loadData);
    }

    protected void initTable() {
        tableModel = new DefaultTableModel(getColumnNames(), 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        scrollPane = new JScrollPane(table);
    }

    protected void initButtons() {
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        addButton = new JButton("Добавить");
        editButton = new JButton("Редактировать");
        deleteButton = new JButton("Удалить");
        refreshButton = new JButton("Обновить");
        exportButton = new JButton("Экспорт в Excel");

        addButton.addActionListener(this::addAction);
        editButton.addActionListener(this::editAction);
        deleteButton.addActionListener(this::deleteAction);
        refreshButton.addActionListener(e -> loadData());
        exportButton.addActionListener(this::exportAction);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(exportButton);
    }

    protected abstract String[] getColumnNames();

    protected abstract void loadData();

    protected abstract void addAction(ActionEvent e);

    protected abstract void editAction(ActionEvent e);

    protected abstract void deleteAction(ActionEvent e);

    protected void exportAction(ActionEvent e) {
        String title = getExportTitle();
        ExcelExporter.exportToExcel(table, title);
    }

    protected String getExportTitle() {
        return "Exported_Data";
    }

    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    protected void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Информация", JOptionPane.INFORMATION_MESSAGE);
    }

    protected int getSelectedRowId() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Выберите запись");
            return -1;
        }
        return selectedRow;
    }
}

