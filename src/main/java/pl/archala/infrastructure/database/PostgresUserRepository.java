package pl.archala.infrastructure.database;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import pl.archala.domain.user.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostgresUserRepository extends BaseJpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);

    List<User> findAll();

}
