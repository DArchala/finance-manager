package pl.archala.domain.user;

public interface UserRepositoryInterface {

    User persistNew(User user);

    User findUserByUsername(String username);

}
