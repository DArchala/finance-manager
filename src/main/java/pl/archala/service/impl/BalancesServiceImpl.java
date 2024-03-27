package pl.archala.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.archala.dto.balance.GetBalanceDTO;
import pl.archala.entity.Balance;
import pl.archala.entity.User;
import pl.archala.enums.BalanceCode;
import pl.archala.exception.InsufficientFundsException;
import pl.archala.exception.UserAlreadyContainsBalance;
import pl.archala.mapper.BalanceMapper;
import pl.archala.repository.BalancesRepository;
import pl.archala.repository.UsersRepository;
import pl.archala.service.BalancesService;

import java.math.BigDecimal;
import java.util.Optional;

import static pl.archala.utils.StringInfoProvider.*;

@Service
@RequiredArgsConstructor
@Transactional
public class BalancesServiceImpl implements BalancesService {

    private final BalancesRepository balancesRepository;
    private final UsersRepository usersRepository;
    private final BalanceMapper balanceMapper;

    @Override
    public GetBalanceDTO create(BalanceCode balanceCode, String username) throws UserAlreadyContainsBalance {
        User user = findUserByUsername(username);

        if (user.getBalance() != null) {
            throw new UserAlreadyContainsBalance(USER_ALREADY_CONTAINS_BALANCE.formatted(user.getUsername()));
        }

        Balance balance = new Balance();
        balance.setUser(user);
        balance.add(balanceCode.getValue());

        Balance savedBalance = balancesRepository.save(balance);
        user.setBalance(savedBalance);

        return balanceMapper.toGetDto(savedBalance);
    }

    @Override
    public synchronized GetBalanceDTO makeTransaction(Long fromBalanceId, Long toBalanceId, BigDecimal value) throws InsufficientFundsException {
        Balance fromBalance = findBalanceById(fromBalanceId);
        if (fromBalance.containsAtLeast(value)) {
            throw new InsufficientFundsException(INSUFFICIENT_FUNDS.formatted(value.longValueExact()));
        }
        Balance toBalance = findBalanceById(toBalanceId);

        fromBalance.subtract(value);
        toBalance.add(value);

        return balanceMapper.toGetDto(fromBalance);
    }

    private Balance findBalanceById(Long id) {
        Optional<Balance> optionalBalance = balancesRepository.findById(id);
        if (optionalBalance.isEmpty()) {
            throw new EntityNotFoundException(BALANCE_WITH_ID_DOES_NOT_EXIST.formatted(id));
        }
        return optionalBalance.get();
    }

    private User findUserByUsername(String username) {
        Optional<User> optionalUser = usersRepository.findUserByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException(USER_WITH_USERNAME_DOES_NOT_EXIST.formatted(username));
        }
        return optionalUser.get();
    }

}
