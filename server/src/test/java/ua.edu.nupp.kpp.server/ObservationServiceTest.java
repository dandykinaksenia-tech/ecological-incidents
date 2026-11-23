package ua.edu.nupp.kpp.server;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ObservationServiceTest {

    private final ObservationService service =
            new ObservationService();

    private Observation createObservation(
            Long id,
            Long incidentId,
            String date,
            String name,
            double value,
            String unit
    ) {
        return new Observation(
                id,
                incidentId,
                date == null ? null : LocalDateTime.parse(date),
                name,
                value,
                unit,
                "Тестовий опис",
                "Тестування"
        );
    }

    // 1. Перевірка порожнього спостереження
    @Test
    void rejectsNullObservation() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.add(null)
        );
    }

    // 2. Перевірка порожнього ID інциденту
    @Test
    void rejectsNullIncidentId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.add(createObservation(
                        null, null, "2026-11-21T10:00:00",
                        "Температура", 25.5, "°C"
                ))
        );
    }

    // 3. Перевірка некоректного ID інциденту
    @Test
    void rejectsInvalidIncidentId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.add(createObservation(
                        null, 0L, "2026-11-21T10:00:00",
                        "Температура", 25.5, "°C"
                ))
        );
    }

    // 4. Перевірка відсутньої дати
    @Test
    void rejectsNullObservationDate() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.add(createObservation(
                        null, 1L, null,
                        "Температура", 25.5, "°C"
                ))
        );
    }

    // 5. Перевірка порожньої назви показника
    @Test
    void rejectsNullIndicatorName() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.add(createObservation(
                        null, 1L, "2026-11-21T10:00:00",
                        null, 25.5, "°C"
                ))
        );
    }

    // 6. Перевірка назви з пробілів
    @Test
    void rejectsBlankIndicatorName() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.add(createObservation(
                        null, 1L, "2026-11-21T10:00:00",
                        "   ", 25.5, "°C"
                ))
        );
    }

    // 7. Перевірка NaN
    @Test
    void rejectsNaNValue() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.add(createObservation(
                        null, 1L, "2026-11-21T10:00:00",
                        "Температура", Double.NaN, "°C"
                ))
        );
    }

    // 8. Перевірка нескінченного значення
    @Test
    void rejectsInfiniteValue() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.add(createObservation(
                        null, 1L, "2026-11-21T10:00:00",
                        "Температура",
                        Double.POSITIVE_INFINITY, "°C"
                ))
        );
    }

    // 9. Перевірка відсутньої одиниці вимірювання
    @Test
    void rejectsNullUnit() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.add(createObservation(
                        null, 1L, "2026-11-21T10:00:00",
                        "Температура", 25.5, null
                ))
        );
    }

    // 10. Перевірка порожньої одиниці вимірювання
    @Test
    void rejectsBlankUnit() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.add(createObservation(
                        null, 1L, "2026-11-21T10:00:00",
                        "Температура", 25.5, "   "
                ))
        );
    }

    // 11. Перевірка оновлення без ID спостереження
    @Test
    void rejectsUpdateWithoutId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.update(createObservation(
                        null, 1L, "2026-11-21T10:00:00",
                        "Температура", 25.5, "°C"
                ))
        );
    }

    // 12. Перевірка отримання за некоректним ID
    @Test
    void rejectsInvalidGetById() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.getById(0)
        );
    }

    // 13. Перевірка видалення за некоректним ID
    @Test
    void rejectsInvalidDeleteId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.delete(-1)
        );
    }
}