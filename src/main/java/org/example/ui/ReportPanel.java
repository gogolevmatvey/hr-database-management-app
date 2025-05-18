package org.example.ui;

import org.example.report.EmployeesByDepartmentReport;

import javax.swing.*;
import java.awt.*;

public class ReportPanel extends JPanel {

    public ReportPanel() {
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        // Panel for report buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Button for employees by department report
        JButton employeesByDeptButton = new JButton("Список сотрудников по отделам");
        employeesByDeptButton.addActionListener(e -> generateEmployeesByDepartmentReport());

        buttonPanel.add(employeesByDeptButton);

        // Add button panel to the top
        add(buttonPanel, BorderLayout.NORTH);

        // Add description panel to the center
        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setLayout(new BorderLayout());

        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setText("Выберите отчет для генерации из списка доступных отчетов выше.");
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
}
