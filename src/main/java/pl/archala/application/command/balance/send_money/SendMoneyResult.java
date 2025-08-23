package pl.archala.application.command.balance.send_money;

import java.math.BigDecimal;

public record SendMoneyResult(String id, BigDecimal value, int dailyTransactionsCount) {
}
