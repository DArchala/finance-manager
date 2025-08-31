package pl.archala.application.command.balance.send_money;

import java.math.BigDecimal;

public record SendMoneyCommand(String username,
                               String targetBalanceId,
                               BigDecimal value) {
}
