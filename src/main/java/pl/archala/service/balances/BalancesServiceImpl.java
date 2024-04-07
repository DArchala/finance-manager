package pl.archala.service.balances;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.archala.dto.balance.BalanceTransactionDTO;
import pl.archala.dto.balance.GetBalanceDTO;
import pl.archala.entity.Balance;
import pl.archala.entity.User;
import pl.archala.enums.BalanceCode;
import pl.archala.exception.InsufficientFundsException;
import pl.archala.exception.TransactionsLimitException;
import pl.archala.exception.UserAlreadyContainsBalanceException;
import pl.archala.exception.UserException;
import pl.archala.mapper.BalancesMapper;
import pl.archala.repository.BalancesRepository;
import pl.archala.repository.UsersRepository;
import pl.archala.service.users.UsersValidator;

import static pl.archala.utils.ExceptionInfoProvider.*;

@Service
@RequiredArgsConstructor
@Transactional
class BalancesServiceImpl implements BalancesService {

    private final BalancesRepository balancesRepository;
    private final BalancesMapper balancesMapper;
    private final BalancesValidator balancesValidator;
    private final UsersRepository usersRepository;
    private final UsersValidator usersValidator;

    @Override
    public GetBalanceDTO create(BalanceCode balanceCode, String username) throws UserAlreadyContainsBalanceException {
        User user = usersRepository.findUserByUsername(username).orElseThrow(() -> new EntityNotFoundException(USER_WITH_USERNAME_DOES_NOT_EXIST.formatted(username)));

        if (user.getBalance() != null) {
            throw new UserAlreadyContainsBalanceException(USER_ALREADY_CONTAINS_BALANCE.formatted(user.getUsername()));
        }

        Balance balance = new Balance();
        balance.add(balanceCode.getValue());

        Balance savedBalance = balancesRepository.save(balance);
        user.setBalance(savedBalance);

        return balancesMapper.toGetDto(savedBalance);
    }

    @Override
    public synchronized GetBalanceDTO makeTransaction(BalanceTransactionDTO transactionDTO, String username) throws InsufficientFundsException, TransactionsLimitException, UserException {
        usersValidator.validateUserBeforeTransaction(username, transactionDTO.sourceBalanceId());
        Balance sourceBalance = balancesRepository.findById(transactionDTO.sourceBalanceId()).orElseThrow(() -> new EntityNotFoundException(BALANCE_WITH_ID_DOES_NOT_EXIST.formatted(transactionDTO.sourceBalanceId())));

        balancesValidator.validateBalanceBeforeTransaction(sourceBalance, transactionDTO.value());
        Balance targetBalance = balancesRepository.findById(transactionDTO.targetBalanceId()).orElseThrow(() -> new EntityNotFoundException(BALANCE_WITH_ID_DOES_NOT_EXIST.formatted(transactionDTO.targetBalanceId())));

        sourceBalance.subtract(transactionDTO.value());
        targetBalance.add(transactionDTO.value());

        sourceBalance.incrementTransactions();

        return balancesMapper.toGetDto(sourceBalance);
    }

}
