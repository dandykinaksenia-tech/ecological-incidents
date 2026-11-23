package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class IncidentService {
    private static final Logger LOG = Logger.getLogger(IncidentService.class.getName());
    private final IncidentDAO dao = new IncidentDAO();
    private final LocationDAO locationDAO = new LocationDAO();

    public List<Incident> getAll() throws SQLException {return dao.getAll();}

    public Incident getById(long id) throws SQLException {
        if (id <= 0) {throw new IllegalArgumentException("Некоректний ID інциденту");}
        return dao.getById(id);
    }

    public void add(Incident incident) throws SQLException {
        incident.setStatus(IncidentStatus.REGISTERED);
        validate(incident); checkLocation(incident.getLocationId());
        double integralScore=RiskCalculator.calculate(incident.getF1(),incident.getF2(),incident.getF3());
        incident.setIntegralScore(integralScore);
        dao.add(incident);
        LOG.info("Зареєстровано екологічний інцидент "+incident.getTitle()+". Інтегральний показник: "+integralScore);
    }

    public void update(Incident incident) throws SQLException {
        if (incident == null) {throw new IllegalArgumentException("Інцидент не може бути порожнім");}
        if (incident.getId() == null || incident.getId() <= 0) {throw new IllegalArgumentException("Некоректний ID інциденту");}
        Incident existing = dao.getById(incident.getId());
        if (existing == null) {throw new IllegalArgumentException("Інцидент не знайдено");}
        incident.setStatus(existing.getStatus());
        incident.setCreatedAt(existing.getCreatedAt());
        validate(incident);
        checkLocation(incident.getLocationId());
        double integralScore = RiskCalculator.calculate(
                incident.getF1(),
                incident.getF2(),
                incident.getF3()
        );
        incident.setIntegralScore(integralScore);
        dao.update(incident);
        LOG.info("Оновлено екологічний інцидент " + incident.getId() + ". Інтегральний показник: " + integralScore);
    }

    public void changeStatus(long id, IncidentStatus newStatus) throws SQLException {
        if(id<=0) throw new IllegalArgumentException("Некоректний ID інциденту");
        if(newStatus==null) throw new IllegalArgumentException("Статус не може бути порожнім");
        Incident incident=dao.getById(id);
        if(incident==null) throw new IllegalArgumentException("Інцидент не знайдено");
        if(!isAllowedTransition(incident.getStatus(),newStatus)) {
            LOG.warning("Недопустимий перехід стану: "+incident.getStatus()+" -> "+newStatus);
            throw new IllegalArgumentException("Недозволений перехід між статусами");
        }
        dao.updateStatus(id,newStatus);
        LOG.info("Змінено стан інциденту: "+incident.getStatus()+" -> "+newStatus);
    }

    public static boolean isAllowedTransition(IncidentStatus status, IncidentStatus newStatus) {
        if(status==null || newStatus==null) return false;
        return (status==IncidentStatus.REGISTERED && newStatus==IncidentStatus.UNDER_REVIEW)
                || (status==IncidentStatus.UNDER_REVIEW && newStatus==IncidentStatus.IN_PROGRESS)
                || (status==IncidentStatus.IN_PROGRESS && newStatus==IncidentStatus.CLOSED);
    }

    public void delete(long id) throws SQLException {
        if(id<=0) throw new IllegalArgumentException("Некоректний ID інциденту");
        dao.delete(id);
        LOG.info("Видалено екологічний інцидент");
    }

    static void validate(Incident incident) {
        if(incident==null) throw new IllegalArgumentException("Інцидент не може бути порожнім");
        if(incident.getTitle()==null || incident.getTitle().isBlank())
            throw new IllegalArgumentException("Назва інциденту не може бути порожньою");
        if(incident.getIncidentType()==null || incident.getIncidentType().isBlank())
            throw new IllegalArgumentException("Тип інциденту не може бути порожнім");
        if(incident.getLocationId()==null || incident.getLocationId()<=0)
            throw new IllegalArgumentException("Необхідно вказати коректний ID локації");
        if(!Double.isFinite(incident.getF1()) || incident.getF1()<0 || incident.getF1()>100)
            throw new IllegalArgumentException("F1 повинен бути в межах від 0 до 100");
        if(!Double.isFinite(incident.getF2()) || incident.getF2()<0 || incident.getF2()>100)
            throw new IllegalArgumentException("F2 повинен бути в межах від 0 до 100");
        if(!Double.isFinite(incident.getF3()) || incident.getF3()<0 || incident.getF3()>100)
            throw new IllegalArgumentException("F3 повинен бути в межах від 0 до 100");
        if(incident.getStatus()==null)
            throw new IllegalArgumentException("Необхідно вказати статус інциденту");
        if(incident.getCreatedAt()==null)
            throw new IllegalArgumentException("Необхідно вказати дату створення інциденту");
    }

    private void checkLocation(Long locationId) throws SQLException {
        if(locationDAO.getById(locationId)==null)
            throw new IllegalArgumentException("Локацію з ID "+locationId+" не знайдено");
    }
}