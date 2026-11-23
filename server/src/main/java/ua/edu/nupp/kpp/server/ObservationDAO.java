package ua.edu.nupp.kpp.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ObservationDAO {
        public void add(Observation o) throws SQLException {
            String sql = "INSERT INTO observation(incident_id,observation_date,indicator_name,indicator_value,unit,description,source) VALUES(?,?,?,?,?,?,?)";
            try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
                bind(p,o); p.executeUpdate();
            }
        }

    public List<Observation> getAll() throws SQLException {
        List<Observation> list = new ArrayList<>();
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement("SELECT * FROM observation ORDER BY id"); ResultSet r = p.executeQuery()) {
            while (r.next()) list.add(map(r));
        }
        return list;
    }

    public Observation getById(long id) throws SQLException {
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement("SELECT * FROM observation WHERE id=?")) {
            p.setLong(1,id); try (ResultSet r = p.executeQuery()) { return r.next() ? map(r) : null; }
        }
    }

    public void update(Observation o) throws SQLException {
        String sql = "UPDATE observation SET incident_id=?,observation_date=?,indicator_name=?,indicator_value=?,unit=?,description=?,source=? WHERE id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            bind(p,o); p.setLong(8,o.getId()); p.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement("DELETE FROM observation WHERE id=?")) {
            p.setLong(1,id); p.executeUpdate();
        }
    }

    private void bind(PreparedStatement p, Observation o) throws SQLException {
        p.setLong(1,o.getIncidentId()); p.setTimestamp(2,Timestamp.valueOf(o.getObservationDate()));
        p.setString(3,o.getIndicatorName()); p.setDouble(4,o.getIndicatorValue());
        p.setString(5,o.getUnit()); p.setString(6,o.getDescription()); p.setString(7,o.getSource());
    }

    private Observation map(ResultSet r) throws SQLException {
        return new Observation(r.getLong("id"),r.getLong("incident_id"),r.getTimestamp("observation_date").toLocalDateTime(),
                r.getString("indicator_name"),r.getDouble("indicator_value"),r.getString("unit"),
                r.getString("description"),r.getString("source"));
    }
}