package pl.archala.entity;

import jakarta.persistence.*;
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

    private int dailyTransactionsCount = 0;

    public void subtract(BigDecimal value) {
        this.value = this.value.subtract(value);
    }

    public void add(BigDecimal value) {
        this.value = this.value.add(value);
    }

    public boolean containsAtLeast(BigDecimal value) {
        return this.value.compareTo(value) >= 0;
    }

    public void incrementTransactions() {
        dailyTransactionsCount++;
    }
}
