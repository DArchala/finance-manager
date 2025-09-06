package pl.archala.application.query.find_user_balance_details;

import java.math.BigDecimal;

public record FindUserBalanceDetailsView(Long balanceId,
                                         BigDecimal balanceAmount,
                                         int dailyTransactionsCount) {
}
