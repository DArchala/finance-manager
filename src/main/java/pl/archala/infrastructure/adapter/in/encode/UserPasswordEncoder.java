package pl.archala.infrastructure.adapter.in.encode;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.archala.domain.user.EncodePassword;

@RequiredArgsConstructor
public class UserPasswordEncoder implements EncodePassword {

    private final PasswordEncoder passwordEncoder;

    @Override
    public char[] encode(String password) {
        return passwordEncoder.encode(password)
                              .toCharArray();
    }
}
