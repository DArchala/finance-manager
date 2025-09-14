package pl.archala.application.command.balance.create;

import pl.archala.application.api.error.ApplicationException;
import pl.archala.application.api.error.ErrorCode;
import pl.archala.domain.balance.Balance;
import pl.archala.domain.balance.GenerateBalanceIdentifier;
import pl.archala.domain.balance.BalanceRepositoryPort;
import pl.archala.domain.user.UserRepositoryPort;
import pl.archala.shared.TransactionExecutor;

public record CreateBalanceApplicationService(UserRepositoryPort userRepositoryPort,
                                              BalanceRepositoryPort balanceRepositoryPort,
                                              TransactionExecutor transactionExecutor,
                                              GenerateBalanceIdentifier generateBalanceIdentifier) {

    public CreateBalanceResult createBalance(CreateBalanceCommand command) {
        var user = userRepositoryPort.findByName(command.balanceOwnerUserName());

        if (user.hasBalance()) {
            throw ApplicationException.from("User with name: %s already has balance.".formatted(command.balanceOwnerUserName()), ErrorCode.UNPROCESSABLE_ENTITY);
        }

        var balance = transactionExecutor.executeInTransactionAndReturn(() -> {
            var persistedBalance = balanceRepositoryPort.persistNew(Balance.create(generateBalanceIdentifier.generate(),
                                                                                   command.balanceCode()
                                                                                          .getValue(),
                                                                                   user));
            user.updateBalance(persistedBalance);
            return persistedBalance;
        });

        return new CreateBalanceResult(balance.getGeneratedId());
    }

}
