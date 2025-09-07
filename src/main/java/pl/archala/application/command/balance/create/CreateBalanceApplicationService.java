package pl.archala.application.command.balance.create;

import pl.archala.application.api.error.ApplicationException;
import pl.archala.application.api.error.ErrorCode;
import pl.archala.domain.balance.Balance;
import pl.archala.domain.balance.BalanceIdentifierGeneratorInterface;
import pl.archala.domain.balance.BalanceRepositoryInterface;
import pl.archala.domain.user.UserRepositoryInterface;
import pl.archala.shared.TransactionExecutor;

import java.util.Objects;

public record CreateBalanceApplicationService(UserRepositoryInterface userRepository,
                                              BalanceRepositoryInterface balanceRepository,
                                              TransactionExecutor transactionExecutor,
                                              BalanceIdentifierGeneratorInterface balanceIdentifierGenerator) {

    public CreateBalanceResult createBalance(CreateBalanceCommand command) {
        var user = userRepository.findUserByUsername(command.username());

        if (Objects.nonNull(user.getBalance())) {
            throw ApplicationException.from("User with name: %s already contains balance.".formatted(command.username()), ErrorCode.UNPROCESSABLE_ENTITY);
        }

        var balance = transactionExecutor.executeInTransactionAndReturn(() -> {
            var persistedBalance = balanceRepository.persistNew(Balance.create(balanceIdentifierGenerator.generate(),
                                                                               command.balanceCode()
                                                                                      .getValue(),
                                                                               user));
            user.updateBalance(persistedBalance);
            return persistedBalance;
        });

        return new CreateBalanceResult(balance.getId());
    }

}
