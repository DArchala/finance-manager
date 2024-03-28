package pl.archala.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.archala.entity.User;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByPhone(String phone);

    @Query("select u from User u join fetch u.balance")
    Optional<User> findUserByUsernameWithBalance(String username);
}
