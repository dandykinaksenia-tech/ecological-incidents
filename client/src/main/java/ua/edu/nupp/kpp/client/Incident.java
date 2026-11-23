package ua.edu.nupp.kpp.client;

public class Incident {
    private Long id;
    private String title;
    private String description;
    private String incidentType;
    private Long locationId;
    private double f1;
    private double f2;
    private double f3;
    private double integralScore;
    private IncidentStatus status;
    private String createdAt;

    public Incident() {}

    public Incident(Long id, String title, String description,
                    String incidentType, Long locationId,
                    double f1, double f2, double f3,
                    double integralScore, IncidentStatus status,
                    String createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.incidentType = incidentType;
        this.locationId = locationId;
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
        this.integralScore = integralScore;
        this.status = status;
        this.createdAt = createdAt;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
    public String getIncidentType() {
        return incidentType;
    }
    public void setIncidentType(String incidentType) {
        this.incidentType = incidentType;
    }
    public Long getLocationId() {
        return locationId;
    }
    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }
    public double getF1() {
        return f1;
    }
    public void setF1(double f1) {
        this.f1 = f1;
    }
    public double getF2() {
        return f2;
    }
    public void setF2(double f2) {
        this.f2 = f2;
    }
    public double getF3() {
        return f3;
    }
    public void setF3(double f3) {
        this.f3 = f3;
    }
    public double getIntegralScore() {
        return integralScore;
    }
    public void setIntegralScore(double integralScore) {
        this.integralScore = integralScore;
    }
    public IncidentStatus getStatus() {
        return status;
    }
    public void setStatus(IncidentStatus status) {
        this.status = status;
    }
    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}