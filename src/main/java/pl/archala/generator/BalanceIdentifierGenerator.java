package pl.archala.generator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class BalanceIdentifierGenerator implements IdentifierGenerator {

    private final int balanceIdLength;

    public BalanceIdentifierGenerator(@Value("${balance-id-length}") int balanceIdLength) {
        this.balanceIdLength = balanceIdLength;
    }

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        String characters = "0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(balanceIdLength);
        for (int i = 0; i < balanceIdLength; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

}
