package pl.archala.service.balances;

import org.springframework.stereotype.Service;
import pl.archala.entity.Balance;
import pl.archala.exception.InsufficientFundsException;
import pl.archala.exception.TransactionsLimitException;

import java.math.BigDecimal;

import static pl.archala.utils.ExceptionInfoProvider.insufficientFunds;
import static pl.archala.utils.ExceptionInfoProvider.transactionsLimitExceeded;

@Service
class BalancesValidatorImpl implements BalancesValidator {

    @Override
    public void validateBalanceBeforeTransaction(Balance balance, BigDecimal value) throws InsufficientFundsException, TransactionsLimitException {
        validateMinimumBalanceContent(balance, value);
        validateDailyTransactionsLimit(balance);
    }

    private void validateMinimumBalanceContent(Balance balance, BigDecimal value) throws InsufficientFundsException {
        if (!balance.containsAtLeast(value)) {
            throw new InsufficientFundsException(insufficientFunds(balance.getId()));
        }
    }

    private void validateDailyTransactionsLimit(Balance balance) throws TransactionsLimitException {
        if (balance.getDailyTransactionsCount() >= 3) {
            throw new TransactionsLimitException(transactionsLimitExceeded(balance.getId()));
        }
    }

}
