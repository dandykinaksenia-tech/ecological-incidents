package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class DaoCheck {
    public static void main(String[] args) throws SQLException {

        LocationDAO locationDAO = new LocationDAO();
        IncidentDAO incidentDAO = new IncidentDAO();
        ObservationDAO observationDAO = new ObservationDAO();
        Response_actionDAO responseActionDAO = new Response_actionDAO();

        Location location = new Location();
        location.setName("DAO-TEST");
        location.setAddress("Полтава");
        location.setLatitude(49.5883);
        location.setLongitude(34.5514);

        locationDAO.add(location);

        List<Location> locations = locationDAO.getAll();
        Location savedLocation = locations.get(locations.size() - 1);
        Long locationId = savedLocation.getId();

        if (locationDAO.getById(locationId) != null)
            System.out.println("Location ADD и READ — OK");

        savedLocation.setName("DAO-TEST (оновлено)");
        locationDAO.update(savedLocation);

        if ("DAO-TEST (оновлено)".equals(locationDAO.getById(locationId).getName()))
            System.out.println("Location UPDATE — OK");

        Incident incident = new Incident();
        incident.setTitle("DAO-TEST");
        incident.setDescription("Тестовий екологічний інцидент");
        incident.setIncidentType("Забруднення повітря");
        incident.setLocationId(locationId);
        incident.setF1(60);
        incident.setF2(70);
        incident.setF3(45);
        incident.setIntegralScore(58.33);
        incident.setStatus(IncidentStatus.REGISTERED);
        incident.setCreatedAt(LocalDateTime.now());

        incidentDAO.add(incident);

        List<Incident> incidents = incidentDAO.getAll();
        Incident savedIncident = incidents.get(incidents.size() - 1);
        Long incidentId = savedIncident.getId();

        if (incidentDAO.getById(incidentId) != null)
            System.out.println("Incident ADD и READ — OK");

        savedIncident.setTitle("DAO-TEST (оновлено)");
        incidentDAO.update(savedIncident);

        if ("DAO-TEST (оновлено)".equals(incidentDAO.getById(incidentId).getTitle()))
            System.out.println("Incident UPDATE — OK");


        Observation observation = new Observation();
        observation.setIncidentId(incidentId);
        observation.setObservationDate(LocalDateTime.now());
        observation.setIndicatorName("Концентрація забруднювальних речовин");
        observation.setIndicatorValue(45.5);
        observation.setUnit("мг/м³");
        observation.setDescription("Тестове спостереження");
        observation.setSource("DAO-TEST");

        observationDAO.add(observation);

        List<Observation> observations = observationDAO.getAll();
        Observation savedObservation = observations.get(observations.size() - 1);
        Long observationId = savedObservation.getId();

        if (observationDAO.getById(observationId) != null)
            System.out.println("Observation ADD и READ — OK");

        savedObservation.setIndicatorValue(50.5);
        observationDAO.update(savedObservation);

        if (observationDAO.getById(observationId).getIndicatorValue() == 50.5)
            System.out.println("Observation UPDATE — OK");

        Response_action responseAction = new Response_action();
        responseAction.setIncidentId(incidentId);
        responseAction.setTitle("DAO-TEST");
        responseAction.setDescription("Тестовий захід реагування");
        responseAction.setResponsiblePerson("Тестова особа");
        responseAction.setPlannedDate(LocalDateTime.now());
        responseAction.setCompletedDate(null);
        responseAction.setStatus("PLANNED");

        responseActionDAO.add(responseAction);

        List<Response_action> actions = responseActionDAO.getAll();
        Response_action savedAction = actions.get(actions.size() - 1);
        Long actionId = savedAction.getId();

        if (responseActionDAO.getById(actionId) != null)
            System.out.println("ResponseAction ADD и READ — OK");

        savedAction.setTitle("DAO-TEST (оновлено)");
        responseActionDAO.update(savedAction);

        if ("DAO-TEST (оновлено)".equals(responseActionDAO.getById(actionId).getTitle()))
            System.out.println("ResponseAction UPDATE — OK");

        responseActionDAO.delete(actionId);
        if (responseActionDAO.getById(actionId) == null)
            System.out.println("ResponseAction DELETE — OK");

        observationDAO.delete(observationId);
        if (observationDAO.getById(observationId) == null)
            System.out.println("Observation DELETE — OK");

        incidentDAO.delete(incidentId);
        if (incidentDAO.getById(incidentId) == null)
            System.out.println("Incident DELETE — OK");

        locationDAO.delete(locationId);
        if (locationDAO.getById(locationId) == null)
            System.out.println("Location DELETE — OK");

        System.out.println("CRUD-перевірка завершена успішно.");
    }
}