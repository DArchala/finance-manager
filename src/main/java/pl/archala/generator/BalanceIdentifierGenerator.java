package pl.archala.generator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class BalanceIdentifierGenerator implements IdentifierGenerator {

    private final int length;
    private final SecureRandom random;
    private final String characters;

    public BalanceIdentifierGenerator(@Value("${balance.id.length}") int length,
                                      @Value("${balance.id.characters}") String characters) {
        this.length = length;
        this.random = new SecureRandom();
        this.characters = characters;
    }

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

}
