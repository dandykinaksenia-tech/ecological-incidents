package ua.edu.nupp.kpp.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RiskCalculatorTest {

    // 1. Перевірка звичайного розрахунку
    @Test
    void calculatesWeightedScore() {
        assertEquals(
                59.75,
                RiskCalculator.calculate(60, 70, 45),
                0.001
        );
    }

    // 2. Перевірка нульових факторів
    @Test
    void calculatesZeroScore() {
        assertEquals(
                0.0,
                RiskCalculator.calculate(0, 0, 0),
                0.001
        );
    }

    // 3. Перевірка максимальних факторів
    @Test
    void calculatesMaximumScore() {
        assertEquals(
                100.0,
                RiskCalculator.calculate(100, 100, 100),
                0.001
        );
    }

    // 4. Перевірка ваги першого фактора
    @Test
    void calculatesFirstFactorWeight() {
        assertEquals(
                40.0,
                RiskCalculator.calculate(100, 0, 0),
                0.001
        );
    }

    // 5. Перевірка ваги другого фактора
    @Test
    void calculatesSecondFactorWeight() {
        assertEquals(
                35.0,
                RiskCalculator.calculate(0, 100, 0),
                0.001
        );
    }

    // 6. Перевірка ваги третього фактора
    @Test
    void calculatesThirdFactorWeight() {
        assertEquals(
                25.0,
                RiskCalculator.calculate(0, 0, 100),
                0.001
        );
    }

    // 7. Перевірка округлення до двох знаків
    @Test
    void roundsResultToTwoDecimalPlaces() {
        assertEquals(
                0.51,
                RiskCalculator.calculate(1.27, 0, 0),
                0.001
        );
    }

    // 8. Перевірка від'ємного першого фактора
    @Test
    void rejectsNegativeFirstFactor() {
        assertThrows(
                IllegalArgumentException.class,
                () -> RiskCalculator.calculate(-1, 50, 50)
        );
    }

    // 9. Перевірка фактора більше 100
    @Test
    void rejectsSecondFactorAbove100() {
        assertThrows(
                IllegalArgumentException.class,
                () -> RiskCalculator.calculate(50, 101, 50)
        );
    }

    // 10. Перевірка NaN
    @Test
    void rejectsNaNFactor() {
        assertThrows(
                IllegalArgumentException.class,
                () -> RiskCalculator.calculate(50, 50, Double.NaN)
        );
    }

    // 11. Перевірка позитивної нескінченності
    @Test
    void rejectsPositiveInfinity() {
        assertThrows(
                IllegalArgumentException.class,
                () -> RiskCalculator.calculate(
                        Double.POSITIVE_INFINITY, 50, 50
                )
        );
    }

    // 12. Перевірка негативної нескінченності
    @Test
    void rejectsNegativeInfinity() {
        assertThrows(
                IllegalArgumentException.class,
                () -> RiskCalculator.calculate(
                        50, Double.NEGATIVE_INFINITY, 50
                )
        );
    }
}