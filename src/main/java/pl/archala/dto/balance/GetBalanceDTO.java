package pl.archala.dto.balance;

import java.math.BigDecimal;

public record GetBalanceDTO(String id, BigDecimal value, int dailyTransactionsCount) {

}
