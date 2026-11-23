package ua.edu.nupp.kpp.server;

import java.time.LocalDateTime;

public class Observation {
    private Long id;
    private Long incidentId;
    private LocalDateTime observationDate;
    private String indicatorName;
    private double indicatorValue;
    private String unit;
    private String description;
    private String source;

    public Observation() {}

    public Observation(Long id, Long incidentId,
                       LocalDateTime observationDate,
                       String indicatorName, double indicatorValue,
                       String unit, String description, String source) {
        this.id = id; this.incidentId = incidentId;
        this.observationDate = observationDate; this.indicatorName = indicatorName;
        this.indicatorValue = indicatorValue;this.unit = unit;
        this.description = description; this.source = source;
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
    public LocalDateTime getObservationDate() {
        return observationDate;
    }
    public void setObservationDate(LocalDateTime observationDate) {
        this.observationDate = observationDate;
    }
    public String getIndicatorName() {
        return indicatorName;
    }
    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }
    public double getIndicatorValue() {
        return indicatorValue;
    }
    public void setIndicatorValue(double indicatorValue) {
        this.indicatorValue = indicatorValue;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
}
