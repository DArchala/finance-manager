package pl.archala.application.command.user.create;

import pl.archala.domain.user.User;
import pl.archala.domain.user.EncodePassword;
import pl.archala.domain.user.UserRepositoryPort;
import pl.archala.shared.TransactionExecutor;

public record CreateUserApplicationService(EncodePassword encodePassword,
                                           TransactionExecutor transactionExecutor,
                                           UserRepositoryPort userRepositoryPort) {

    public CreateUserResult createUser(CreateUserCommand command) {
        var user = User.create(command.name(),
                               encodePassword.encode(command.password()),
                               command.phone(),
                               command.email(),
                               command.notificationChannel());

        return transactionExecutor.executeInTransactionAndReturn(() -> new CreateUserResult(userRepositoryPort.persistNew(user)
                                                                                                              .getExternalUuid()));
    }

}
