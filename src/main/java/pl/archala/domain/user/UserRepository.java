package pl.archala.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.archala.infrastructure.database.postgres.PostgresUserRepository;
import pl.archala.infrastructure.exception.ApplicationException;

@RequiredArgsConstructor
@Repository
public class UserRepository {

    private final PostgresUserRepository postgresUserRepository;

    public User persistNew(User user) {
        return postgresUserRepository.persist(user);
    }

    public User findUserByUsername(String username) {
        return postgresUserRepository.findUserByUsername(username)
                                     .orElseThrow(() -> ApplicationException.notFound("User with username: %s, not found".formatted(username)));
    }

}
