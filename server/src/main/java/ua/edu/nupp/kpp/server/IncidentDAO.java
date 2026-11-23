package ua.edu.nupp.kpp.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class IncidentDAO {
    public void add(Incident i) throws SQLException {
        String sql = "INSERT INTO incident(title, description, incident_type, " + "location_id, f1, f2, f3, integral_score, status, created_at) " + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            bind(p, i);
            p.executeUpdate();
        }
    }
    public List<Incident> getAll() throws SQLException {
        List<Incident> list = new ArrayList<>();
        String sql = "SELECT * FROM incident ORDER BY id";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql); ResultSet r = p.executeQuery()) {
            while (r.next()) {
                list.add(map(r));
            }
        }
        return list;
    }
    public Incident getById(long id) throws SQLException {
        String sql = "SELECT * FROM incident WHERE id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? map(r) : null;
            }
        }
    }

    public void update(Incident i) throws SQLException {
        String sql = "UPDATE incident SET title = ?, description = ?, " + "incident_type = ?, location_id = ?, f1 = ?, f2 = ?, " + "f3 = ?, integral_score = ?, created_at = ? WHERE id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setString(1, i.getTitle());
            p.setString(2, i.getDescription());
            p.setString(3, i.getIncidentType());
            p.setLong(4, i.getLocationId());
            p.setDouble(5, i.getF1());
            p.setDouble(6, i.getF2());
            p.setDouble(7, i.getF3());
            p.setDouble(8, i.getIntegralScore());
            p.setTimestamp(9, Timestamp.valueOf(i.getCreatedAt()));
            p.setLong(10, i.getId());

            p.executeUpdate();
        }
    }

    public void updateStatus(long id, IncidentStatus status)
            throws SQLException {
        String sql = "UPDATE incident SET status = ? WHERE id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, status.name());
            p.setLong(2, id);
            p.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM incident WHERE id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            p.executeUpdate();
        }
    }

    private void bind(PreparedStatement p, Incident i) throws SQLException {
        p.setString(1, i.getTitle());
        p.setString(2, i.getDescription());
        p.setString(3, i.getIncidentType());
        p.setLong(4, i.getLocationId());
        p.setDouble(5, i.getF1());
        p.setDouble(6, i.getF2());
        p.setDouble(7, i.getF3());
        p.setDouble(8, i.getIntegralScore());
        p.setString(9, i.getStatus().name());
        p.setTimestamp(10, Timestamp.valueOf(i.getCreatedAt()));
    }

    private Incident map(ResultSet r) throws SQLException {
        Timestamp timestamp = r.getTimestamp("created_at");
        Incident i = new Incident(
                r.getLong("id"),
                r.getString("title"),
                r.getString("description"),
                r.getString("incident_type"),
                r.getLong("location_id"),
                r.getDouble("f1"),
                r.getDouble("f2"),
                r.getDouble("f3"),
                r.getDouble("integral_score"),
                getStatus(r.getString("status")),
                timestamp != null ? timestamp.toLocalDateTime() : null
        );

        return i;
    }

    private IncidentStatus getStatus(String status) {
        if (status == null || status.isBlank()) {throw new IllegalArgumentException("Статус інциденту відсутній");
        }

        switch (status) {
            case "Зареєстровано":
                return IncidentStatus.REGISTERED;
            case "На розгляді":
                return IncidentStatus.UNDER_REVIEW;
            case "У роботі":
                return IncidentStatus.IN_PROGRESS;
            case "Закрито":
                return IncidentStatus.CLOSED;
            default:
                return IncidentStatus.valueOf(status);
        }
    }
}