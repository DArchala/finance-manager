package pl.archala.application.command.balance.create;

import lombok.RequiredArgsConstructor;
import pl.archala.application.api.error.ApplicationException;
import pl.archala.application.api.error.ErrorCode;
import pl.archala.domain.balance.Balance;
import pl.archala.domain.balance.BalanceRepositoryInterface;
import pl.archala.domain.user.UserRepositoryInterface;
import pl.archala.shared.TransactionExecutor;

import java.util.Objects;

@RequiredArgsConstructor
public class CreateBalanceApplicationService {

    private final UserRepositoryInterface userRepository;
    private final BalanceRepositoryInterface balanceRepository;
    private final TransactionExecutor transactionExecutor;

    public CreateBalanceResult createBalance(CreateBalanceCommand command) {
        var user = userRepository.findUserByUsername(command.username());

        if (Objects.nonNull(user.getBalance())) {
            throw ApplicationException.from("User with name: %s already contains balance.".formatted(command.username()), ErrorCode.UNPROCESSABLE_ENTITY);
        }

        var balance = transactionExecutor.executeInTransactionAndReturn(() -> {
            var persistedBalance = balanceRepository.persistNew(Balance.create(null, command.balanceCode()
                                                                                            .getValue(),
                                                                               user));
            user.updateBalance(persistedBalance);
            return persistedBalance;
        });

        return new CreateBalanceResult(balance.getId());
    }

}
