package pl.archala.service.balances;

import pl.archala.dto.balance.GetBalanceDTO;
import pl.archala.enums.BalanceCode;
import pl.archala.exception.*;

import java.math.BigDecimal;

public interface BalancesService {

    GetBalanceDTO create(BalanceCode balanceCode, String username) throws UserAlreadyContainsBalance;

    GetBalanceDTO makeTransaction(Long fromBalanceId, Long toBalanceId, BigDecimal value, String username) throws InsufficientFundsException, TransactionsLimitException, UsersConflictException, UserException;
}
