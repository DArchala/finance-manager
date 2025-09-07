package pl.archala.domain.user;

public interface UserRepositoryPort {

    User persistNew(User user);

    User findUserByUsername(String username);

}
