package pl.archala.infrastructure.adapter.in.encode;

import org.springframework.security.crypto.password.PasswordEncoder;
import pl.archala.domain.user.UserPasswordEncoder;

public record UserPasswordEncoderImpl(PasswordEncoder passwordEncoder) implements UserPasswordEncoder {

    @Override
    public char[] encode(String password) {
        return passwordEncoder.encode(password)
                              .toCharArray();
    }
}
