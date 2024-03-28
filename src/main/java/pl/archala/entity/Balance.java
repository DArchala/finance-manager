package pl.archala.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "balances")
@Entity
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal value = BigDecimal.ZERO;

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
        this.value = this.value.add(value);
    }

    public boolean containsAtLeast(BigDecimal value) {
        return this.value.compareTo(value) >= 0;
    }

    public synchronized void incrementTransactions() {
        dailyTransactionsCount++;
    }
}
