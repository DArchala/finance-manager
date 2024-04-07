package pl.archala.service.balances;

import org.springframework.stereotype.Service;
import pl.archala.entity.Balance;
import pl.archala.exception.InsufficientFundsException;
import pl.archala.exception.TransactionsLimitException;

import java.math.BigDecimal;

import static pl.archala.utils.ExceptionInfoProvider.INSUFFICIENT_FUNDS;
import static pl.archala.utils.ExceptionInfoProvider.TRANSACTIONS_LIMIT_EXCEEDED;

@Service
class BalancesValidatorImpl implements BalancesValidator {

    @Override
    public void validateBalanceBeforeTransaction(Balance balance, BigDecimal value) throws InsufficientFundsException, TransactionsLimitException {
        validateMinimumBalanceContent(balance, value);
        validateDailyTransactionsLimit(balance);
    }

    private void validateMinimumBalanceContent(Balance balance, BigDecimal value) throws InsufficientFundsException {
        if (!balance.containsAtLeast(value)) {
            throw new InsufficientFundsException(INSUFFICIENT_FUNDS.formatted(balance.getId()));
        }
    }

    private void validateDailyTransactionsLimit(Balance balance) throws TransactionsLimitException {
        if (balance.getDailyTransactionsCount() >= 3) {
            throw new TransactionsLimitException(TRANSACTIONS_LIMIT_EXCEEDED.formatted(balance.getId()));
        }
    }

}
