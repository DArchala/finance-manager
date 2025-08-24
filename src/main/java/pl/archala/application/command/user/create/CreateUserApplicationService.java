package pl.archala.application.command.user.create;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.archala.shared.TransactionExecutor;
import pl.archala.domain.user.User;
import pl.archala.domain.user.UserRepository;

@RequiredArgsConstructor
@Service
public class CreateUserApplicationService {

    private final PasswordEncoder passwordEncoder;
    private final TransactionExecutor transactionExecutor;
    private final UserRepository userRepository;

    public CreateUserResult createUser(CreateUserCommand command) {
        var user = User.create(command.username(),
                               passwordEncoder.encode(command.password())
                                              .toCharArray(),
                               command.phone(),
                               command.email(),
                               command.notificationChannel());

        return transactionExecutor.executeInTransactionAndReturn(() -> new CreateUserResult(userRepository.persistNew(user)
                                                                                                          .getExternalUuid()));
    }

}
