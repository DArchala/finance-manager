package pl.archala.domain.balance;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import pl.archala.domain.user.User;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "balance-id")
    @GenericGenerator(name = "balance-id", type = BalanceIdentifierGenerator.class)
    private String id;

    private BigDecimal amount;

    @OneToOne(mappedBy = "balance")
    private User user;

    private int dailyTransactionsCount = 0;

    public void subtract(BigDecimal amount) {
        this.amount = this.amount.subtract(amount);
    }

    public void add(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ArithmeticException("It is unavailable to add negative amount to balance");
        }
        this.amount = this.amount.add(amount);
    }

    public boolean containsAtLeast(BigDecimal amount) {
        return this.amount.compareTo(amount) >= 0;
    }

    public void incrementTransactions() {
        dailyTransactionsCount++;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Balance balance)) {
            return false;
        }
        return dailyTransactionsCount == balance.dailyTransactionsCount && Objects.equals(id, balance.id) && Objects.equals(amount, balance.amount) &&
               Objects.equals(user, balance.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, dailyTransactionsCount);
    }

    public static Balance create(BigDecimal value, User user) {
        return new Balance(null, value, user, 0);
    }

    public void updateUser(User user) {
        this.user = user;
    }
}
