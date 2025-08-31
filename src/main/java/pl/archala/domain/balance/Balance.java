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

    @OneToOne(mappedBy = "balance", cascade = {CascadeType.ALL})
    private User user;

    @Min(0)
    @Max(3)
    private volatile int dailyTransactionsCount = 0;

    public synchronized void subtract(BigDecimal value) {
        this.amount = this.amount.subtract(value);
    }

    public synchronized void add(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new ArithmeticException("It is unavailable to add negative amount to balance");
        }
        this.amount = this.amount.add(value);
    }

    public synchronized boolean containsAtLeast(BigDecimal value) {
        return this.amount.compareTo(value) >= 0;
    }

    public synchronized void incrementTransactions() {
        dailyTransactionsCount++;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Balance balance)) {
            return false;
        }
        return dailyTransactionsCount == balance.dailyTransactionsCount && Objects.equals(id, balance.id) && Objects.equals(amount, balance.amount);
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
