package pl.archala.domain.balance;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import pl.archala.domain.user.User;
import pl.archala.infrastructure.generator.BalanceIdentifierGenerator;

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

    private BigDecimal value;

    @OneToOne(mappedBy = "balance", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private User user;

    @Min(0)
    @Max(3)
    private volatile int dailyTransactionsCount = 0;

    public synchronized void subtract(BigDecimal value) {
        this.value = this.value.subtract(value);
    }

    public synchronized void add(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new ArithmeticException("It is unavailable to add negative amount to balance");
        }
        this.value = this.value.add(value);
    }

    public synchronized boolean containsAtLeast(BigDecimal value) {
        return this.value.compareTo(value) >= 0;
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
        return dailyTransactionsCount == balance.dailyTransactionsCount && Objects.equals(id, balance.id) && Objects.equals(value, balance.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, dailyTransactionsCount);
    }

    public static Balance create(BigDecimal value, User user) {
        return new Balance(null, value, user, 0);
    }
}
