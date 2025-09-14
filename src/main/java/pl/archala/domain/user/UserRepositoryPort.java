package pl.archala.domain.user;

public interface UserRepositoryPort {

    User persistNew(User user);

    User findByName(String name);

}
