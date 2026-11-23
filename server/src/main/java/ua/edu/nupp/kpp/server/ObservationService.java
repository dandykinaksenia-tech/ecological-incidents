package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class ObservationService {
    private static final Logger LOG = Logger.getLogger(ObservationService.class.getName());
    private final ObservationDAO dao = new ObservationDAO();
    private final IncidentDAO incidentDAO = new IncidentDAO();

    public List<Observation> getAll() throws SQLException {
        return dao.getAll();
    }

    public Observation getById(long id) throws SQLException {
        if (id <= 0) throw new IllegalArgumentException("Некоректний ID спостереження");
        return dao.getById(id);
    }

    public void add(Observation observation) throws SQLException {
        validate(observation);
        checkIncident(observation.getIncidentId());
        dao.add(observation);
        LOG.info("Додано спостереження " + observation.getIndicatorName());
    }

    public void update(Observation observation) throws SQLException {
        validate(observation);
        if (observation.getId() == null || observation.getId() <= 0)
            throw new IllegalArgumentException("Некоректний ID спостереження");
        checkIncident(observation.getIncidentId());
        dao.update(observation);
        LOG.info("Оновлено спостереження " + observation.getId());
    }

    public void delete(long id) throws SQLException {
        if (id <= 0) throw new IllegalArgumentException("Некоректний ID спостереження");
        dao.delete(id);
        LOG.info("Видалено спостереження " + id);
    }

    private void checkIncident(Long incidentId) throws SQLException {
        if (incidentDAO.getById(incidentId) == null)
            throw new IllegalArgumentException("Інцидент з ID " + incidentId + " не знайдено");
    }

    private void validate(Observation observation) {
        if (observation == null)
            throw new IllegalArgumentException("Спостереження не може бути порожнім");
        if (observation.getIncidentId() == null || observation.getIncidentId() <= 0)
            throw new IllegalArgumentException("Необхідно вказати коректний ID інциденту");
        if (observation.getObservationDate() == null)
            throw new IllegalArgumentException("Необхідно вказати дату спостереження");
        if (observation.getIndicatorName() == null || observation.getIndicatorName().trim().isEmpty())
            throw new IllegalArgumentException("Назва показника не може бути порожньою");
        if (!Double.isFinite(observation.getIndicatorValue()))
            throw new IllegalArgumentException("Значення показника повинно бути коректним числом");
        if (observation.getUnit() == null || observation.getUnit().trim().isEmpty())
            throw new IllegalArgumentException("Одиниця вимірювання не може бути порожньою");
    }
}