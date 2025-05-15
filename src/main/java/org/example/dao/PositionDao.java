package org.example.dao;

import org.example.db.DatabaseConnection;
import org.example.model.Position;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PositionDao {

    public List<Position> getAllPositions() throws SQLException {
        List<Position> positions = new ArrayList<>();
        String sql = "SELECT * FROM Position";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Position position = new Position();
                position.setId(rs.getLong("id"));
                position.setName(rs.getString("name"));
                position.setDescription(rs.getString("description"));
                position.setDay_payment(rs.getInt("day_payment"));

                positions.add(position);
            }
        }

        return positions;
    }

    public Position getPositionById(long id) throws SQLException {
        String sql = "SELECT * FROM Position WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Position position = new Position();
                    position.setId(rs.getLong("id"));
                    position.setName(rs.getString("name"));
                    position.setDescription(rs.getString("description"));
                    position.setDay_payment(rs.getInt("day_payment"));

                    return position;
                }
            }
        }

        return null;
    }

    public void addPosition(Position position) throws SQLException {
        long newId = getMaxPositionId() + 1;
        position.setId(newId);

        String sql = "INSERT INTO Position (id, name, description, day_payment) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, position.getId());
            pstmt.setString(2, position.getName());
            pstmt.setString(3, position.getDescription());
            pstmt.setInt(4, position.getDay_payment());

            pstmt.executeUpdate();
        }
    }

    public void updatePosition(Position position) throws SQLException {
        String sql = "UPDATE Position SET name = ?, description = ?, day_payment = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, position.getName());
            pstmt.setString(2, position.getDescription());
            pstmt.setInt(3, position.getDay_payment());
            pstmt.setLong(4, position.getId());

            pstmt.executeUpdate();
        }
    }

    public void deletePosition(long id) throws SQLException {
        String sql = "DELETE FROM Position WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }

    private long getMaxPositionId() throws SQLException {
        String sql = "SELECT MAX(id) FROM Position";

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

