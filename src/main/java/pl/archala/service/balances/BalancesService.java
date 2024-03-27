package pl.archala.service.balances;

import pl.archala.dto.balance.GetBalanceDTO;
import pl.archala.enums.BalanceCode;
import pl.archala.exception.InsufficientFundsException;
import pl.archala.exception.TransactionsLimitException;
import pl.archala.exception.UserAlreadyContainsBalance;
import pl.archala.exception.UsersConflictException;

import java.math.BigDecimal;

public interface BalancesService {

    GetBalanceDTO create(BalanceCode balanceCode, String username) throws UserAlreadyContainsBalance;

    GetBalanceDTO makeTransaction(Long fromBalanceId, Long toBalanceId, BigDecimal value) throws InsufficientFundsException, TransactionsLimitException, UsersConflictException;
}
