package pl.archala.domain.balance;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum BalanceCode {

    CODE_1(100),
    CODE_2(200),
    CODE_3(300),
    CODE_4(400),
    CODE_5(500);

    private final BigDecimal value;

    BalanceCode(int value) {
        this.value = BigDecimal.valueOf(value);
    }
}
