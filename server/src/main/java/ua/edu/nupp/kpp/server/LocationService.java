package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class LocationService {
    private static final Logger LOG = Logger.getLogger(LocationService.class.getName());
    private final LocationDAO dao = new LocationDAO();

    public List<Location> getAll() throws SQLException {return dao.getAll();}
    public Location getById(long id) throws SQLException {return dao.getById(id);}

    public void add(Location l) throws SQLException {
        validate(l);dao.add(l);LOG.info("Додано місце виникнення інциденту");
    }

    public void update(Location l) throws SQLException {
        validate(l);dao.update(l);LOG.info("Оновлено місце виникнення інциденту");
    }

    public void delete(long id) throws SQLException {
        dao.delete(id);LOG.info("Видалено місце виникнення інциденту");
    }

    static void validate(Location l) {
        if (l == null || blank(l.getName()) || blank(l.getRegion()) || blank(l.getAddress()))
            throw new IllegalArgumentException("Усі обов'язкові поля локації мають бути заповнені");

        if (l.getLatitude() != null &&
                (!Double.isFinite(l.getLatitude()) || l.getLatitude() < -90 || l.getLatitude() > 90))
            throw new IllegalArgumentException("Некоректна широта");

        if (l.getLongitude() != null &&
                (!Double.isFinite(l.getLongitude()) || l.getLongitude() < -180 || l.getLongitude() > 180))
            throw new IllegalArgumentException("Некоректна довгота");
    }

    private static boolean blank(String v) { return v == null || v.isBlank(); }
}