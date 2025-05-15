package org.example.dao;

import org.example.db.DatabaseConnection;
import org.example.model.Document;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DocumentDao {

    public List<Document> getAllDocuments() throws SQLException {
        List<Document> documents = new ArrayList<>();
        String sql = "SELECT * FROM Document";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Document document = new Document();
                document.setId(rs.getLong("id"));
                document.setType(rs.getInt("type"));
                document.setSerial(rs.getInt("serial"));
                document.setNumber(rs.getLong("number"));
                document.setDate(rs.getDate("date").toLocalDate());
                document.setGiven_by(rs.getString("given_by"));
                document.setEmployee_id(rs.getLong("employee_id"));

                documents.add(document);
            }
        }

        return documents;
    }

    public Document getDocumentById(long id) throws SQLException {
        String sql = "SELECT * FROM Document WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Document document = new Document();
                    document.setId(rs.getLong("id"));
                    document.setType(rs.getInt("type"));
                    document.setSerial(rs.getInt("serial"));
                    document.setNumber(rs.getLong("number"));
                    document.setDate(rs.getDate("date").toLocalDate());
                    document.setGiven_by(rs.getString("given_by"));
                    document.setEmployee_id(rs.getLong("employee_id"));

                    return document;
                }
            }
        }

        return null;
    }

    public List<Document> getDocumentsByEmployeeId(long employeeId) throws SQLException {
        List<Document> documents = new ArrayList<>();
        String sql = "SELECT * FROM Document WHERE employee_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, employeeId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Document document = new Document();
                    document.setId(rs.getLong("id"));
                    document.setType(rs.getInt("type"));
                    document.setSerial(rs.getInt("serial"));
                    document.setNumber(rs.getLong("number"));
                    document.setDate(rs.getDate("date").toLocalDate());
                    document.setGiven_by(rs.getString("given_by"));
                    document.setEmployee_id(rs.getLong("employee_id"));

                    documents.add(document);
                }
            }
        }

        return documents;
    }

    public void addDocument(Document document) throws SQLException {
        long newId = getMaxDocumentId() + 1;
        document.setId(newId);

        String sql = "INSERT INTO Document (id, type, serial, number, date, given_by, employee_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, document.getId());
            pstmt.setInt(2, document.getType());
            pstmt.setInt(3, document.getSerial());
            pstmt.setLong(4, document.getNumber());
            pstmt.setDate(5, Date.valueOf(document.getDate()));
            pstmt.setString(6, document.getGiven_by());
            pstmt.setLong(7, document.getEmployee_id());

            pstmt.executeUpdate();
        }
    }

    public void updateDocument(Document document) throws SQLException {
        String sql = "UPDATE Document SET type = ?, serial = ?, number = ?, date = ?, given_by = ?, employee_id = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, document.getType());
            pstmt.setInt(2, document.getSerial());
            pstmt.setLong(3, document.getNumber());
            pstmt.setDate(4, Date.valueOf(document.getDate()));
            pstmt.setString(5, document.getGiven_by());
            pstmt.setLong(6, document.getEmployee_id());
            pstmt.setLong(7, document.getId());

            pstmt.executeUpdate();
        }
    }

    public void deleteDocument(long id) throws SQLException {
        String sql = "DELETE FROM Document WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }

    private long getMaxDocumentId() throws SQLException {
        String sql = "SELECT MAX(id) FROM Document";

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



