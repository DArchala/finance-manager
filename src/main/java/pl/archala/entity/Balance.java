package pl.archala.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import pl.archala.generator.BalanceIdentifierGenerator;
import pl.archala.utils.BigDecimalProvider;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Table(name = "balances")
@Entity
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "balance-id")
    @GenericGenerator(name = "balance-id", type = BalanceIdentifierGenerator.class)
    private String id;

    private volatile BigDecimal value = BigDecimalProvider.DEFAULT_VALUE;

    @Setter
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
            throw new ArithmeticException("It is unavailable to add negative value to balance");
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
        if (this == object) return true;
        if (!(object instanceof Balance balance)) return false;
        return dailyTransactionsCount == balance.dailyTransactionsCount && Objects.equals(id, balance.id) && Objects.equals(value, balance.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, dailyTransactionsCount);
    }
}
