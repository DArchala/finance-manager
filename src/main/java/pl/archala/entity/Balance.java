package pl.archala.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import static pl.archala.utils.StringInfoProvider.SUBTRACTING_BIGGER_VALUE;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "balances")
@Entity
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal value = new BigDecimal("0");

    @Setter
    @OneToOne(mappedBy = "balance", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private User user;

    public void subtract(BigDecimal value) {
        if (!containsAtLeast(value)) {
            throw new ArithmeticException(SUBTRACTING_BIGGER_VALUE);
        }
        this.value = this.value.subtract(value);
    }

    public void add(BigDecimal value) {
        this.value = this.value.add(value);
    }

    public boolean containsAtLeast(BigDecimal value) {
        return this.value.compareTo(value) >= 0;
    }

}
