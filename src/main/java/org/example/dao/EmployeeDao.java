package org.example.dao;

import org.example.db.DatabaseConnection;
import org.example.model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDao {

    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employee";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Employee employee = new Employee();
                employee.setId(rs.getInt("id"));
                employee.setFull_name(rs.getString("full_name"));
                employee.setPhone_number(rs.getLong("phone_number"));
                employee.setEducation(rs.getString("education"));

                employees.add(employee);
            }
        }

        return employees;
    }

    public Employee getEmployeeById(int id) throws SQLException {
        String sql = "SELECT * FROM Employee WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Employee employee = new Employee();
                    employee.setId(rs.getInt("id"));
                    employee.setFull_name(rs.getString("full_name"));
                    employee.setPhone_number(rs.getLong("phone_number"));
                    employee.setEducation(rs.getString("education"));

                    return employee;
                }
            }
        }

        return null;
    }

    public void addEmployee(Employee employee) throws SQLException {
        int newId = getMaxEmployeeId() + 1;
        employee.setId(newId);

        String sql = "INSERT INTO Employee (id, full_name, phone_number, education) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, employee.getId());
            pstmt.setString(2, employee.getFull_name());
            pstmt.setLong(3, employee.getPhone_number());
            pstmt.setString(4, employee.getEducation());

            pstmt.executeUpdate();
        }
    }

    public void updateEmployee(Employee employee) throws SQLException {
        String sql = "UPDATE Employee SET full_name = ?, phone_number = ?, education = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, employee.getFull_name());
            pstmt.setLong(2, employee.getPhone_number());
            pstmt.setString(3, employee.getEducation());
            pstmt.setInt(4, employee.getId());

            pstmt.executeUpdate();
        }
    }

    public void deleteEmployee(int id) throws SQLException {
        String sql = "DELETE FROM Employee WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    private int getMaxEmployeeId() throws SQLException {
        String sql = "SELECT MAX(id) FROM Employee";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    public List<Employee> getEmployeesByDepartmentId(long departmentId) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT e.* FROM Employee e " +
                "JOIN Employment_contract ec ON e.id = ec.employee_id " +
                "WHERE ec.department_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, departmentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Employee employee = new Employee();
                    employee.setId(rs.getInt("id"));
                    employee.setFull_name(rs.getString("full_name"));
                    employee.setPhone_number(rs.getLong("phone_number"));
                    employee.setEducation(rs.getString("education"));

                    employees.add(employee);
                }
            }
        }

        return employees;
    }
}


