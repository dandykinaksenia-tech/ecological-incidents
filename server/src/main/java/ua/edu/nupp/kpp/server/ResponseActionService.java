package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class ResponseActionService {
    private static final Logger LOG = Logger.getLogger(ResponseActionService.class.getName());
    private final Response_actionDAO dao = new Response_actionDAO();
    private final IncidentDAO incidentDAO = new IncidentDAO();

    public List<Response_action> getAll() throws SQLException {return dao.getAll();}

    public Response_action getById(long id) throws SQLException {
        if (id <= 0)
            throw new IllegalArgumentException("Некоректний ID заходу реагування");
        return dao.getById(id);
    }

    public void add(Response_action action) throws SQLException {
        validate(action);
        checkIncident(action.getIncidentId());
        dao.add(action);
        LOG.info("Додано захід реагування");
    }

    public void update(Response_action action) throws SQLException {
        validate(action);
        if (action.getId() == null || action.getId() <= 0)
            throw new IllegalArgumentException("Некоректний ID заходу реагування");
        checkIncident(action.getIncidentId());
        dao.update(action);
        LOG.info("Оновлено захід реагування");
    }

    public void delete(long id) throws SQLException {
        if (id <= 0)
            throw new IllegalArgumentException("Некоректний ID заходу реагування");
        dao.delete(id);
        LOG.info("Видалено захід реагування");
    }

    private void checkIncident(Long incidentId) throws SQLException {
        if (incidentDAO.getById(incidentId) == null)
            throw new IllegalArgumentException("Інцидент з ID " + incidentId + " не знайдено");
    }

    private void validate(Response_action action) {
        if (action == null)
            throw new IllegalArgumentException("Захід реагування не може бути порожнім");
        if (action.getIncidentId() == null || action.getIncidentId() <= 0)
            throw new IllegalArgumentException("Необхідно вказати коректний ID інциденту");
        if (action.getTitle() == null || action.getTitle().trim().isEmpty())
            throw new IllegalArgumentException("Назва заходу реагування не може бути порожньою");
        if (action.getPlannedDate() == null)
            throw new IllegalArgumentException("Необхідно вказати заплановану дату");
        if (action.getStatus() == null || action.getStatus().trim().isEmpty())
            throw new IllegalArgumentException("Необхідно вказати статус заходу реагування");
    }
}
