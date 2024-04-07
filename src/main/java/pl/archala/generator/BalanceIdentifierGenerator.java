package pl.archala.generator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.List;

@Component
public class BalanceIdentifierGenerator implements IdentifierGenerator {

    private final int length;
    private final SecureRandom random;
    private final List<Character> characters;

    public BalanceIdentifierGenerator(@Value("${balance.id.length}") int length,
                                      @Value("${balance.id.characters}") String characters) {
        this.length = length;
        this.random = new SecureRandom();
        this.characters = characters.chars().mapToObj(c -> (char) c).distinct().toList();
    }

    /*
    Generator should check if generated balance id is not already present in the DB
    but 20-digits password can have 10^20 combinations, so for this demo app it is
    sufficient, and we do not have to worry about it
     */

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(characters.get(random.nextInt(characters.size())));
        }
        return sb.toString();
    }

}
