package pl.archala.infrastructure.adapter.out.persistence.user;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.jetbrains.annotations.TestOnly;
import pl.archala.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface JpaUserRepository extends BaseJpaRepository<User, Long> {

    Optional<User> findByName(String name);

    List<User> findAll();

    @TestOnly
    void deleteAll();
}
