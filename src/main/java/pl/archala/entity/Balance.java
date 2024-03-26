package pl.archala.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Table(name = "balances")
@Entity
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal value;

    @OneToOne(mappedBy = "balance")
    private User user;

    public void substract(BigDecimal value) {
        this.value = this.value.subtract(value);
    }

    public void add(BigDecimal value) {
        this.value = this.value.add(value);
    }

    public boolean containsAtLeast(BigDecimal value) {
        return this.value.compareTo(value) > 0;
    }

}
