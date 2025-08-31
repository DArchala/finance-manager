package pl.archala.application.command.balance.create;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.archala.domain.exception.ApplicationException;
import pl.archala.shared.TransactionExecutor;
import pl.archala.domain.balance.Balance;
import pl.archala.domain.balance.BalanceRepository;
import pl.archala.domain.user.UserRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CreateBalanceApplicationService {

    private final UserRepository userRepository;
    private final BalanceRepository balanceRepository;
    private final TransactionExecutor transactionExecutor;

    public CreateBalanceResult createBalance(CreateBalanceCommand command) {
        var user = userRepository.findUserByUsername(command.username());

        if (Objects.nonNull(user.getBalance())) {
            throw ApplicationException.from("User with name: %s already contains balance.".formatted(command.username()), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var balance = transactionExecutor.executeInTransactionAndReturn(() -> {
            var persistedBalance = balanceRepository.persistNew(Balance.create(command.balanceCode()
                                                                                      .getValue(),
                                                                               user));
            user.updateBalance(persistedBalance);
            return persistedBalance;
        });

        return new CreateBalanceResult(balance.getId());
    }

}
