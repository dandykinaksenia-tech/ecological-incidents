package ua.edu.nupp.kpp.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class HttpEcologicalIncidentsClient {
    private static final String BASE = "http://localhost:8080/ecological-incidents-server";
    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Location> getLocations() {return Arrays.asList(read(get("/locations"), Location[].class));}
    public void addLocation(Location l) {post("/locations", locationForm("add", l));}
    public void updateLocation(Location l) {post("/locations", locationForm("update", l) + "&id=" + l.getId());}
    public void deleteLocation(long id) {post("/locations", "action=delete&id=" + id);}

    public List<Incident> getIncidents() {return Arrays.asList(read(get("/incidents"), Incident[].class));}
    public void addIncident(Incident i) {post("/incidents", incidentForm("add", i));}
    public void updateIncident(Incident i) {post("/incidents", incidentForm("update", i) + "&id=" + i.getId());}
    public void changeIncidentStatus(long id, IncidentStatus status) {post("/incidents", "action=transition&id=" + id + "&status=" + status.name());}
    public void deleteIncident(long id) {post("/incidents", "action=delete&id=" + id);}

    public List<Observation> getObservations() {return Arrays.asList(read(get("/observations"), Observation[].class));}
    public void addObservation(Observation o) {post("/observations", observationForm("add", o));}
    public void updateObservation(Observation o) {post("/observations", observationForm("update", o) + "&id=" + o.getId());}
    public void deleteObservation(long id) {post("/observations", "action=delete&id=" + id);}

    public List<ResponseAction> getResponseActions() {return Arrays.asList(read(get("/response-actions"), ResponseAction[].class));}
    public void addResponseAction(ResponseAction a) {post("/response-actions", responseActionForm("add", a));}
    public void updateResponseAction(ResponseAction a) {post("/response-actions", responseActionForm("update", a) + "&id=" + a.getId());}
    public void deleteResponseAction(long id) {post("/response-actions", "action=delete&id=" + id);}

    private String locationForm(String action, Location l) {
        return "action=" + action + "&name=" + enc(l.getName()) + "&region=" + enc(l.getRegion()) +
                "&address=" + enc(l.getAddress()) + "&latitude=" + l.getLatitude() +
                "&longitude=" + l.getLongitude() + "&description=" + enc(l.getDescription());
    }

    private String incidentForm(String action, Incident i) {
        return "action=" + action + "&title=" + enc(i.getTitle()) + "&description=" + enc(i.getDescription()) +
                "&incidentType=" + enc(i.getIncidentType()) + "&locationId=" + i.getLocationId() +
                "&f1=" + i.getF1() + "&f2=" + i.getF2() + "&f3=" + i.getF3();
    }

    private String observationForm(String action, Observation o) {
        return "action=" + action + "&incidentId=" + o.getIncidentId() +
                "&observationDate=" + enc(String.valueOf(o.getObservationDate())) +
                "&indicatorName=" + enc(o.getIndicatorName()) + "&indicatorValue=" + o.getIndicatorValue() +
                "&unit=" + enc(o.getUnit()) + "&description=" + enc(o.getDescription()) +
                "&source=" + enc(o.getSource());
    }

    private String responseActionForm(String action, ResponseAction a) {
        return "action=" + action + "&incidentId=" + a.getIncidentId() + "&title=" + enc(a.getTitle()) +
                "&description=" + enc(a.getDescription()) + "&responsiblePerson=" + enc(a.getResponsiblePerson()) +
                "&plannedDate=" + enc(a.getPlannedDate()) + "&completedDate=" + enc(a.getCompletedDate()) +
                "&status=" + enc(a.getStatus());
    }

    private String get(String path) {
        HttpRequest req = HttpRequest.newBuilder(URI.create(BASE + path)).GET().build();
        return send(req);
    }
    private String post(String path, String form) {
        HttpRequest req = HttpRequest.newBuilder(URI.create(BASE + path))
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(form, StandardCharsets.UTF_8))
                .build();
        return send(req);
    }
    private String send(HttpRequest req) {
        try {
            HttpResponse<String>
                    r = http.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (r.statusCode() >= 400) {throw new RuntimeException(r.body());}
            return r.body();
        } catch (Exception e) {throw new RuntimeException("Помилка HTTP: " + e.getMessage(), e);}
    }
    private <T> T read(String json, Class<T> type) {
        try {return mapper.readValue(json, type);} catch (Exception e) {throw new RuntimeException("Помилка JSON", e);}
    }
    private String enc(String s) {
        return URLEncoder.encode(s == null ? "" : s, StandardCharsets.UTF_8);
    }
}


