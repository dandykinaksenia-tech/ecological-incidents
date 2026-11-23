package ua.edu.nupp.kpp.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IncidentServiceTest {

    // 1. REGISTERED -> UNDER_REVIEW
    @Test
    void allowsRegisteredToUnderReview() {
        assertTrue(IncidentService.isAllowedTransition(
                IncidentStatus.REGISTERED,
                IncidentStatus.UNDER_REVIEW
        ));
    }

    // 2. UNDER_REVIEW -> IN_PROGRESS
    @Test
    void allowsUnderReviewToInProgress() {
        assertTrue(IncidentService.isAllowedTransition(
                IncidentStatus.UNDER_REVIEW,
                IncidentStatus.IN_PROGRESS
        ));
    }

    // 3. IN_PROGRESS -> CLOSED
    @Test
    void allowsInProgressToClosed() {
        assertTrue(IncidentService.isAllowedTransition(
                IncidentStatus.IN_PROGRESS,
                IncidentStatus.CLOSED
        ));
    }

    // 4. Заборонений перехід REGISTERED -> IN_PROGRESS
    @Test
    void rejectsRegisteredToInProgress() {
        assertFalse(IncidentService.isAllowedTransition(
                IncidentStatus.REGISTERED,
                IncidentStatus.IN_PROGRESS
        ));
    }

    // 5. Заборонений перехід REGISTERED -> CLOSED
    @Test
    void rejectsRegisteredToClosed() {
        assertFalse(IncidentService.isAllowedTransition(
                IncidentStatus.REGISTERED,
                IncidentStatus.CLOSED
        ));
    }

    // 6. Заборонений перехід UNDER_REVIEW -> CLOSED
    @Test
    void rejectsUnderReviewToClosed() {
        assertFalse(IncidentService.isAllowedTransition(
                IncidentStatus.UNDER_REVIEW,
                IncidentStatus.CLOSED
        ));
    }

    // 7. Заборонений зворотний перехід
    @Test
    void rejectsInProgressToRegistered() {
        assertFalse(IncidentService.isAllowedTransition(
                IncidentStatus.IN_PROGRESS,
                IncidentStatus.REGISTERED
        ));
    }

    // 8. Закритий інцидент не можна повернути до роботи
    @Test
    void rejectsClosedToRegistered() {
        assertFalse(IncidentService.isAllowedTransition(
                IncidentStatus.CLOSED,
                IncidentStatus.REGISTERED
        ));
    }

    // 9. Перевірка порожнього початкового стану
    @Test
    void rejectsNullCurrentStatus() {
        assertFalse(IncidentService.isAllowedTransition(
                null,
                IncidentStatus.UNDER_REVIEW
        ));
    }

    // 10. Перевірка порожнього нового стану
    @Test
    void rejectsNullNewStatus() {
        assertFalse(IncidentService.isAllowedTransition(
                IncidentStatus.REGISTERED,
                null
        ));
    }
}