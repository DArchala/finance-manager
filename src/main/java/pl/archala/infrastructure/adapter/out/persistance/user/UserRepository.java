package pl.archala.infrastructure.adapter.out.persistance.user;

import pl.archala.domain.user.UserRepositoryInterface;
import pl.archala.domain.user.User;
import pl.archala.application.api.error.ApplicationException;

public record UserRepository(PostgresUserRepository postgresUserRepository) implements UserRepositoryInterface {

    public User persistNew(User user) {
        return postgresUserRepository.persist(user);
    }

    public User findUserByUsername(String username) {
        return postgresUserRepository.findUserByUsername(username)
                                     .orElseThrow(() -> ApplicationException.notFound("User with username: %s, not found".formatted(username)));
    }

}
