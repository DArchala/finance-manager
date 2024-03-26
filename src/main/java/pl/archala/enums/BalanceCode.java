package pl.archala.enums;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum BalanceCode {

    KOD_1(100),
    KOD_2(200),
    KOD_3(300),
    KOD_4(400),
    KOD_5(500);

    private final BigDecimal value;

    BalanceCode(int value) {
        this.value = BigDecimal.valueOf(value);
    }
}
