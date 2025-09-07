package pl.archala.domain.balance;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.archala.domain.user.User;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String generatedId;

    @Column(nullable = false)
    private BigDecimal amount;

    @OneToOne(mappedBy = "balance")
    private User user;

    @Column(nullable = false)
    private int dailyTransactionsCount = 0;

    @Version
    @Column(nullable = false)
    private Long version;

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

    public static Balance create(BalanceGeneratedIdentifier identifier, BigDecimal value, User user) {
        return new Balance(null, identifier.id(), value, user, 0, 0L);
    }

    public void updateUser(User user) {
        this.user = user;
    }
}
