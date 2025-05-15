package org.example.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;

    public MainFrame() {
        setTitle("HR Department Database");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();

        setVisible(true);
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();

        // Панели для каждой таблицы
        tabbedPane.addTab("Сотрудники", new EmployeePanel());
        tabbedPane.addTab("Отделы", new DepartmentPanel());
        tabbedPane.addTab("Должности", new PositionPanel());
        tabbedPane.addTab("Трудовые договоры", new EmploymentContractPanel());
        tabbedPane.addTab("Документы", new DocumentPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }
}



