package pl.archala.domain.user;

public interface UserRepositoryPort {

    void persistNew(User user);

    User findByName(String name);

}
