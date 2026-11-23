package ua.edu.nupp.kpp.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationDAO {
    public void add(Location l) throws SQLException {
        String sql = "INSERT INTO location(name, region, address, latitude, longitude, description) " + "VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {bind(p, l);
            p.executeUpdate();
        }
    }
    public List<Location> getAll() throws SQLException {
        List<Location> list = new ArrayList<>();
        String sql = "SELECT * FROM location ORDER BY id";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql); ResultSet r = p.executeQuery()) {
            while (r.next()) {list.add(map(r));
            }
        }

        return list;
    }
    public Location getById(long id) throws SQLException {
        String sql = "SELECT * FROM location WHERE id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            try (ResultSet r = p.executeQuery()) {return r.next() ? map(r) : null;
            }
        }
    }

    public void update(Location l) throws SQLException {
        String sql = "UPDATE location SET name = ?, region = ?, address = ?, " + "latitude = ?, longitude = ?, description = ? WHERE id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            bind(p, l);
            p.setLong(7, l.getId());
            p.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM location WHERE id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            p.executeUpdate();
        }
    }

    private void bind(PreparedStatement p, Location l) throws SQLException {
        p.setString(1, l.getName());
        p.setString(2, l.getRegion());
        p.setString(3, l.getAddress());

        if (l.getLatitude() == null) {
            p.setNull(4, Types.DOUBLE);
        } else {
            p.setDouble(4, l.getLatitude());
        }

        if (l.getLongitude() == null) {
            p.setNull(5, Types.DOUBLE);
        } else {
            p.setDouble(5, l.getLongitude());
        }

        p.setString(6, l.getDescription());
    }

    private Location map(ResultSet r) throws SQLException {
        double latitude = r.getDouble("latitude");
        Double lat = r.wasNull() ? null : latitude;
        double longitude = r.getDouble("longitude");
        Double lon = r.wasNull() ? null : longitude;
        return new Location(
                r.getLong("id"),
                r.getString("name"),
                r.getString("region"),
                r.getString("address"),
                lat,
                lon,
                r.getString("description")
        );
    }
}

