package pl.archala.service;

import pl.archala.dto.balance.GetBalanceDTO;
import pl.archala.exception.UserAlreadyContainsBalance;

public interface BalancesService {

    GetBalanceDTO findById(Long id);

    GetBalanceDTO save(String username) throws UserAlreadyContainsBalance;
}
