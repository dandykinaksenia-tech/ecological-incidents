package ua.edu.nupp.kpp.server;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CourseworkTest {
    @Test void riskForDemoDataIs593() {assertEquals(59.3, RiskCalculator.calculate(52, 60, 70));}
    @Test void riskRejectsFactorAbove100() {assertThrows(IllegalArgumentException.class, () -> RiskCalculator.calculate(101, 10, 10));}
    @Test void riskRejectsNegativeFactor() {assertThrows(IllegalArgumentException.class, () -> RiskCalculator.calculate(-1, 10, 10));}
    @Test void riskRejectsInfiniteFactor() {assertThrows(IllegalArgumentException.class, () -> RiskCalculator.calculate(Double.POSITIVE_INFINITY, 10, 10));}
    @Test void registeredToUnderReviewAllowed() {assertTrue(IncidentService.isAllowedTransition(IncidentStatus.REGISTERED, IncidentStatus.UNDER_REVIEW));}
    @Test void underReviewToInProgressAllowed() {assertTrue(IncidentService.isAllowedTransition(IncidentStatus.UNDER_REVIEW, IncidentStatus.IN_PROGRESS));}
    @Test void inProgressToClosedAllowed() {assertTrue(IncidentService.isAllowedTransition(IncidentStatus.IN_PROGRESS, IncidentStatus.CLOSED));}
    @Test void registeredToClosedRejected() {assertFalse(IncidentService.isAllowedTransition(IncidentStatus.REGISTERED, IncidentStatus.CLOSED));}
    @Test void locationValidationWorks() {assertThrows(IllegalArgumentException.class, () -> LocationService.validate(null));}
    @Test void observationValidationWorks() {assertThrows(IllegalArgumentException.class, () -> new ObservationService().add(null));}
}