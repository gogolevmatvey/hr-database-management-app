package org.example.dao;

import org.example.db.DatabaseConnection;
import org.example.model.EmploymentContract;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmploymentContractDao {

    public List<EmploymentContract> getAllContracts() throws SQLException {
        List<EmploymentContract> contracts = new ArrayList<>();
        String sql = "SELECT * FROM Employment_contract";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                EmploymentContract contract = new EmploymentContract();
                contract.setId(rs.getLong("id"));
                contract.setNumber(rs.getInt("number"));
                contract.setType(rs.getString("type"));
                contract.setCreation_date(rs.getDate("creation_date").toLocalDate());
                contract.setDate_of_conclusion(rs.getString("date_of_conclusion"));
                contract.setEmployee_id(rs.getLong("employee_id"));
                contract.setDepartment_id(rs.getLong("department_id"));
                contract.setPosition_id(rs.getLong("position_id"));

                contracts.add(contract);
            }
        }

        return contracts;
    }

    public EmploymentContract getContractById(long id) throws SQLException {
        String sql = "SELECT * FROM Employment_contract WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    EmploymentContract contract = new EmploymentContract();
                    contract.setId(rs.getLong("id"));
                    contract.setNumber(rs.getInt("number"));
                    contract.setType(rs.getString("type"));
                    contract.setCreation_date(rs.getDate("creation_date").toLocalDate());
                    contract.setDate_of_conclusion(rs.getString("date_of_conclusion"));
                    contract.setEmployee_id(rs.getLong("employee_id"));
                    contract.setDepartment_id(rs.getLong("department_id"));
                    contract.setPosition_id(rs.getLong("position_id"));

                    return contract;
                }
            }
        }

        return null;
    }

    public List<EmploymentContract> getContractsByEmployeeId(long employeeId) throws SQLException {
        List<EmploymentContract> contracts = new ArrayList<>();
        String sql = "SELECT * FROM Employment_contract WHERE employee_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, employeeId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    EmploymentContract contract = new EmploymentContract();
                    contract.setId(rs.getLong("id"));
                    contract.setNumber(rs.getInt("number"));
                    contract.setType(rs.getString("type"));
                    contract.setCreation_date(rs.getDate("creation_date").toLocalDate());
                    contract.setDate_of_conclusion(rs.getString("date_of_conclusion"));
                    contract.setEmployee_id(rs.getLong("employee_id"));
                    contract.setDepartment_id(rs.getLong("department_id"));
                    contract.setPosition_id(rs.getLong("position_id"));

                    contracts.add(contract);
                }
            }
        }

        return contracts;
    }

    public List<EmploymentContract> getContractsByDepartmentId(long departmentId) throws SQLException {
        List<EmploymentContract> contracts = new ArrayList<>();
        String sql = "SELECT * FROM Employment_contract WHERE department_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, departmentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    EmploymentContract contract = new EmploymentContract();
                    contract.setId(rs.getLong("id"));
                    contract.setNumber(rs.getInt("number"));
                    contract.setType(rs.getString("type"));
                    contract.setCreation_date(rs.getDate("creation_date").toLocalDate());
                    contract.setDate_of_conclusion(rs.getString("date_of_conclusion"));
                    contract.setEmployee_id(rs.getLong("employee_id"));
                    contract.setDepartment_id(rs.getLong("department_id"));
                    contract.setPosition_id(rs.getLong("position_id"));

                    contracts.add(contract);
                }
            }
        }

        return contracts;
    }

    public void addContract(EmploymentContract contract) throws SQLException {
        long newId = getMaxContractId() + 1;
        contract.setId(newId);

        String sql = "INSERT INTO Employment_contract (id, number, type, creation_date, date_of_conclusion, employee_id, department_id, position_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, contract.getId());
            pstmt.setInt(2, contract.getNumber());
            pstmt.setString(3, contract.getType());
            pstmt.setDate(4, Date.valueOf(contract.getCreation_date()));
            pstmt.setString(5, contract.getDate_of_conclusion());
            pstmt.setLong(6, contract.getEmployee_id());
            pstmt.setLong(7, contract.getDepartment_id());
            pstmt.setLong(8, contract.getPosition_id());

            pstmt.executeUpdate();
        }
    }

    public void updateContract(EmploymentContract contract) throws SQLException {
        String sql = "UPDATE Employment_contract SET number = ?, type = ?, creation_date = ?, date_of_conclusion = ?, employee_id = ?, department_id = ?, position_id = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, contract.getNumber());
            pstmt.setString(2, contract.getType());
            pstmt.setDate(3, Date.valueOf(contract.getCreation_date()));
            pstmt.setString(4, contract.getDate_of_conclusion());
            pstmt.setLong(5, contract.getEmployee_id());
            pstmt.setLong(6, contract.getDepartment_id());
            pstmt.setLong(7, contract.getPosition_id());
            pstmt.setLong(8, contract.getId());

            pstmt.executeUpdate();
        }
    }

    public void deleteContract(long id) throws SQLException {
        String sql = "DELETE FROM Employment_contract WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }

    private long getMaxContractId() throws SQLException {
        String sql = "SELECT MAX(id) FROM Employment_contract";

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

