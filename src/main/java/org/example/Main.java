package org.example;

//import org.example.ui.DatabaseUI;
import org.example.dao.*;
import org.example.db.DatabaseConnection;
import org.example.ui.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            DatabaseConnection.getConnection();
            System.out.println("Успешое подключение к базе данных.");
//            EmployeeDao employeeDao = new EmployeeDao();
//            EmploymentContractDao employmentContractDao = new EmploymentContractDao();
//            PositionDao positionDao = new PositionDao();
//            DocumentDao documentDao = new DocumentDao();
//            DepartmentDao departmentDao = new DepartmentDao();
//            System.out.println(positionDao.getAllPositions());
//            System.out.println(documentDao.getAllDocuments());
//            System.out.println(departmentDao.getAllDepartments());
//            System.out.println(employmentContractDao.getAllContracts());
//            System.out.println(employeeDao.getAllEmployees());

            SwingUtilities.invokeLater(() -> {
                new MainFrame();
            });

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
