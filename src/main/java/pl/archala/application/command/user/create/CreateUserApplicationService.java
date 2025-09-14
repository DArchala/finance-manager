package pl.archala.application.command.user.create;

import pl.archala.domain.user.User;
import pl.archala.domain.user.EncodePassword;
import pl.archala.domain.user.UserRepositoryPort;
import pl.archala.shared.TransactionExecutor;

public record CreateUserApplicationService(EncodePassword encodePassword,
                                           TransactionExecutor transactionExecutor,
                                           UserRepositoryPort userRepositoryPort) {

    public void createUser(CreateUserCommand command) {
        var user = User.create(command.name(),
                               encodePassword.encode(command.password()),
                               command.phone(),
                               command.email(),
                               command.notificationChannel());

        transactionExecutor.executeInTransaction(() -> userRepositoryPort.persistNew(user));
    }

}
