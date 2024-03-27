package pl.archala.service;

import pl.archala.dto.balance.GetBalanceDTO;
import pl.archala.enums.BalanceCode;
import pl.archala.exception.InsufficientFundsException;
import pl.archala.exception.UserAlreadyContainsBalance;

import java.math.BigDecimal;

public interface BalancesService {

    GetBalanceDTO create(BalanceCode balanceCode, String username) throws UserAlreadyContainsBalance;

    GetBalanceDTO makeTransaction(Long fromBalanceId, Long toBalanceId, BigDecimal value) throws InsufficientFundsException;
}
