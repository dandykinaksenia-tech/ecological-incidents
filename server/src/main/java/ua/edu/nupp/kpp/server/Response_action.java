package ua.edu.nupp.kpp.server;

import java.time.LocalDateTime;

public class Response_action {
    private Long id;
    private Long incidentId;
    private String title;
    private String description;
    private String responsiblePerson;
    private LocalDateTime plannedDate;
    private LocalDateTime completedDate;
    private String status;

    public Response_action() {}
    public Response_action(Long id, Long incidentId, String title,
                           String description, String responsiblePerson,
                           LocalDateTime plannedDate,
                           LocalDateTime completedDate, String status) {
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
    public LocalDateTime getPlannedDate() {
        return plannedDate;
    }
    public void setPlannedDate(LocalDateTime plannedDate) {
        this.plannedDate = plannedDate;
    }
    public LocalDateTime getCompletedDate() {
        return completedDate;
    }
    public void setCompletedDate(LocalDateTime completedDate) {
        this.completedDate = completedDate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
