package org.example.dao;

import org.example.db.DatabaseConnection;
import org.example.model.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDao {

    public List<Department> getAllDepartments() throws SQLException {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM Department";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Department department = new Department();
                department.setId(rs.getLong("id"));
                department.setName(rs.getString("name"));

                departments.add(department);
            }
        }

        return departments;
    }

    public Department getDepartmentById(long id) throws SQLException {
        String sql = "SELECT * FROM Department WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Department department = new Department();
                    department.setId(rs.getLong("id"));
                    department.setName(rs.getString("name"));

                    return department;
                }
            }
        }

        return null;
    }

    public void addDepartment(Department department) throws SQLException {
        long newId = getMaxDepartmentId() + 1;
        department.setId(newId);

        String sql = "INSERT INTO Department (id, name) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, department.getId());
            pstmt.setString(2, department.getName());

            pstmt.executeUpdate();
        }
    }

    public void updateDepartment(Department department) throws SQLException {
        String sql = "UPDATE Department SET name = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, department.getName());
            pstmt.setLong(2, department.getId());

            pstmt.executeUpdate();
        }
    }

    public void deleteDepartment(long id) throws SQLException {
        String sql = "DELETE FROM Department WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }

    private long getMaxDepartmentId() throws SQLException {
        String sql = "SELECT MAX(id) FROM Department";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0;
        }
    }
}

