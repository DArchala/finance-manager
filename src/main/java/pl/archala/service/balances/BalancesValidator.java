package pl.archala.service.balances;

import pl.archala.entity.Balance;
import pl.archala.exception.InsufficientFundsException;
import pl.archala.exception.TransactionsLimitException;

import java.math.BigDecimal;

public interface BalancesValidator {

    void validateBalanceToTransaction(Balance balance, BigDecimal value) throws InsufficientFundsException, TransactionsLimitException;
}
