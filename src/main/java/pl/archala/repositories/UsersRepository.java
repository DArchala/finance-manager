package pl.archala.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.archala.entity.User;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {

}
