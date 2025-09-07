package pl.archala.domain.user;

public interface UserPasswordEncoder {

    char[] encode(String password);

}
