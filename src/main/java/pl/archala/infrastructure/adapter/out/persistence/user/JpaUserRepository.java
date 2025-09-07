package pl.archala.infrastructure.adapter.out.persistence.user;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import pl.archala.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface JpaUserRepository extends BaseJpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);

    List<User> findAll();

}
