package ua.edu.nupp.kpp.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Response_actionDAO {
    public void add(Response_action a) throws SQLException {
        String sql = "INSERT INTO response_action(incident_id,title,description,responsible_person,planned_date,completed_date,status) VALUES(?,?,?,?,?,?,?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            bind(p, a);
            p.executeUpdate();
        }
    }

    public List<Response_action> getAll() throws SQLException {
        List<Response_action> list = new ArrayList<>();
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement("SELECT * FROM response_action ORDER BY id"); ResultSet r = p.executeQuery()) {
            while (r.next()) list.add(map(r));
        }
        return list;
    }

    public Response_action getById(long id) throws SQLException {
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement("SELECT * FROM response_action WHERE id=?")) {
            p.setLong(1, id);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? map(r) : null;
            }
        }
    }

    public void update(Response_action a) throws SQLException {
        String sql = "UPDATE response_action SET incident_id=?,title=?,description=?,responsible_person=?,planned_date=?,completed_date=?,status=? WHERE id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            bind(p, a);
            p.setLong(8, a.getId());
            p.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement("DELETE FROM response_action WHERE id=?")) {
            p.setLong(1, id);
            p.executeUpdate();
        }
    }

    private void bind(PreparedStatement p, Response_action a) throws SQLException {
        p.setLong(1, a.getIncidentId());
        p.setString(2, a.getTitle());
        p.setString(3, a.getDescription());
        p.setString(4, a.getResponsiblePerson());
        p.setTimestamp(5, Timestamp.valueOf(a.getPlannedDate()));
        if (a.getCompletedDate() != null) {
            p.setTimestamp(6, Timestamp.valueOf(a.getCompletedDate()));
        } else {
            p.setNull(6, Types.TIMESTAMP);
        }
        p.setString(7, a.getStatus());
    }

    private Response_action map(ResultSet r) throws SQLException {
        Timestamp completedDate = r.getTimestamp("completed_date");
        return new Response_action(
                r.getLong("id"), r.getLong("incident_id"),
                r.getString("title"), r.getString("description"),
                r.getString("responsible_person"),
                r.getTimestamp("planned_date").toLocalDateTime(),
                completedDate == null ? null : completedDate.toLocalDateTime(),
                r.getString("status")
        );
    }
}