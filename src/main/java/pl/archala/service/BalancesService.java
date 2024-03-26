package pl.archala.service;

import pl.archala.dto.balance.GetBalanceDTO;
import pl.archala.enums.BalanceCode;
import pl.archala.exception.UserAlreadyContainsBalance;

public interface BalancesService {

    GetBalanceDTO findById(Long id);

    GetBalanceDTO create(BalanceCode balanceCode, String username) throws UserAlreadyContainsBalance;
}
