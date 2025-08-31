package pl.archala.entity;

import org.junit.jupiter.api.Test;
import pl.archala.domain.balance.Balance;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class BalanceTest {

    @Test
    void shouldReturnTrueIfBalanceContainsAtLeastProvidedValue() {
        //given
        BigDecimal biggerValue = BigDecimal.valueOf(100);
        BigDecimal smallerValue = BigDecimal.valueOf(100);
        Balance balance = new Balance();

        //when
        balance.add(biggerValue);
        boolean actualResult = balance.containsAtLeast(smallerValue);

        //then
        assertTrue(actualResult);

    }

    @Test
    void shouldReturnFalseIfBalanceDoesNotContainAtLeastProvidedValue() {
        //given
        BigDecimal smallerValue = BigDecimal.valueOf(50);
        BigDecimal biggerValue = BigDecimal.valueOf(100);
        Balance balance = new Balance();

        //when
        balance.add(smallerValue);
        boolean actualResult = balance.containsAtLeast(biggerValue);

        //then
        assertFalse(actualResult);

    }

    @Test
    void balanceValueShouldBeEqualToCurrentValuePlusProvidedValueAfterAdding() {
        //given
        BigDecimal value1 = BigDecimal.valueOf(50);
        BigDecimal value2 = BigDecimal.valueOf(100);
        BigDecimal sum = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP).add(value1).add(value2);

        Balance balance = new Balance();

        //when
        balance.add(value1);
        balance.add(value2);

        //then
        assertEquals(sum, balance.getAmount());

    }

    @Test
    void comparingBalancesShouldReturnZeroIfContainEqualValue() {
        //given
        BigDecimal value = BigDecimal.valueOf(50);
        Balance balance1 = new Balance();
        Balance balance2 = new Balance();

        //when
        balance1.add(value);
        balance2.add(value);
        int compare = balance1.getAmount().compareTo(balance2.getAmount());

        //then
        assertEquals(0, compare);

    }

    @Test
    void subtractingBalanceWithEqualValueShouldSetValueToZero() {
        //given
        BigDecimal value = BigDecimal.valueOf(50);
        Balance balance = new Balance();

        //when
        balance.add(value);
        balance.subtract(value);

        //then
        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), balance.getAmount());

    }

    @Test
    void addingNegativeNumbersShouldThrowException() {
        //given
        BigDecimal negativeValue = BigDecimal.valueOf(-50);
        Balance balance = new Balance();

        //when
        //then
        assertThrows(ArithmeticException.class, () -> balance.add(negativeValue));
    }

    @Test
    void addingPositiveOrZeroValueShouldNotThrowException() {
        //given
        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal positiveValue = BigDecimal.valueOf(50);
        Balance balance = new Balance();

        //when
        //then
        assertDoesNotThrow(() -> balance.add(zero));
        assertDoesNotThrow(() -> balance.add(positiveValue));
    }
}