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
import pl.archala.exception.TransactionsLimitException;
import pl.archala.exception.UserAlreadyContainsBalance;
import pl.archala.mapper.BalancesMapper;
import pl.archala.repository.BalancesRepository;
import pl.archala.repository.UsersRepository;
import pl.archala.service.BalancesService;
import pl.archala.service.BalancesValidator;

import java.math.BigDecimal;
import java.util.Optional;

import static pl.archala.utils.StringInfoProvider.*;

@Service
@RequiredArgsConstructor
@Transactional
public class BalancesServiceImpl implements BalancesService {

    private final BalancesRepository balancesRepository;
    private final BalancesMapper balancesMapper;
    private final BalancesValidator balancesValidator;
    private final UsersRepository usersRepository;

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

        return balancesMapper.toGetDto(savedBalance);
    }

    @Override
    public synchronized GetBalanceDTO makeTransaction(Long fromBalanceId, Long toBalanceId, BigDecimal value) throws InsufficientFundsException, TransactionsLimitException {
        Balance sourceBalance = findBalanceById(fromBalanceId);
        balancesValidator.validateBalanceToTransaction(sourceBalance, value);
        Balance targetBalance = findBalanceById(toBalanceId);

        sourceBalance.subtract(value);
        targetBalance.add(value);

        sourceBalance.incrementTransactions();

        return balancesMapper.toGetDto(sourceBalance);
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
