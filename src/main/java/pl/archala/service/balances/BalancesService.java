package pl.archala.service.balances;

import pl.archala.dto.balance.BalanceTransactionDTO;
import pl.archala.dto.balance.GetBalanceDTO;
import pl.archala.enums.BalanceCode;
import pl.archala.exception.InsufficientFundsException;
import pl.archala.exception.TransactionsLimitException;
import pl.archala.exception.UserAlreadyContainsBalanceException;
import pl.archala.exception.UserException;

public interface BalancesService {

    GetBalanceDTO create(BalanceCode balanceCode, String username) throws UserAlreadyContainsBalanceException;

    GetBalanceDTO makeTransaction(BalanceTransactionDTO transactionDTO, String username) throws InsufficientFundsException, TransactionsLimitException, UserException;
}
