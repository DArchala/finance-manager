package pl.archala.application.command.balance.send_money;

import java.math.BigDecimal;

public record SendMoneyCommand(String sourceBalanceId,
                               String targetBalanceId,
                               BigDecimal value,
                               String username) {
}
