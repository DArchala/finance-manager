package pl.archala.infrastructure.database;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import pl.archala.domain.user.User;

import java.util.Optional;

public interface PostgresUserRepository extends BaseJpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);

}
