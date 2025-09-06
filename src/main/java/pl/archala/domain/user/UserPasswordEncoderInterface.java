package pl.archala.domain.user;

public interface UserPasswordEncoderInterface {

    char[] encode(String password);

}
