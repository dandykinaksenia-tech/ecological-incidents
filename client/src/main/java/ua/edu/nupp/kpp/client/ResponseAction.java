package ua.edu.nupp.kpp.client;

public class ResponseAction {
    private Long id;
    private Long incidentId;
    private String title;
    private String description;
    private String responsiblePerson;
    private String plannedDate;
    private String completedDate;
    private String status;

    public ResponseAction() {}

    public ResponseAction(Long id, Long incidentId, String title,
                          String description, String responsiblePerson,
                          String plannedDate, String completedDate,
                          String status) {
        this.id = id;
        this.incidentId = incidentId;
        this.title = title;
        this.description = description;
        this.responsiblePerson = responsiblePerson;
        this.plannedDate = plannedDate;
        this.completedDate = completedDate;
        this.status = status;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getIncidentId() {
        return incidentId;
    }
    public void setIncidentId(Long incidentId) {
        this.incidentId = incidentId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getResponsiblePerson() {
        return responsiblePerson;
    }
    public void setResponsiblePerson(String responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }
    public String getPlannedDate() {
        return plannedDate;
    }
    public void setPlannedDate(String plannedDate) {
        this.plannedDate = plannedDate;
    }
    public String getCompletedDate() {
        return completedDate;
    }
    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}