package pl.archala.infrastructure.adapter.in.encode;

import org.springframework.security.crypto.password.PasswordEncoder;
import pl.archala.domain.user.UserPasswordEncoderInterface;

public record UserPasswordEncoder(PasswordEncoder passwordEncoder) implements UserPasswordEncoderInterface {

    @Override
    public char[] encode(String password) {
        return passwordEncoder.encode(password)
                              .toCharArray();
    }
}
