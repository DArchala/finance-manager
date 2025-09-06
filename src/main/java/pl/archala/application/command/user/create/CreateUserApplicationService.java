package pl.archala.application.command.user.create;

import pl.archala.domain.user.User;
import pl.archala.domain.user.UserPasswordEncoderInterface;
import pl.archala.domain.user.UserRepositoryInterface;
import pl.archala.shared.TransactionExecutor;

public record CreateUserApplicationService(UserPasswordEncoderInterface userPasswordEncoderInterface,
                                           TransactionExecutor transactionExecutor,
                                           UserRepositoryInterface userRepository) {

    public CreateUserResult createUser(CreateUserCommand command) {
        var user = User.create(command.username(),
                               userPasswordEncoderInterface.encode(command.password()),
                               command.phone(),
                               command.email(),
                               command.notificationChannel());

        return transactionExecutor.executeInTransactionAndReturn(() -> new CreateUserResult(userRepository.persistNew(user)
                                                                                                          .getExternalUuid()));
    }

}
