package pl.archala.infrastructure.adapter.in.generate;

import lombok.RequiredArgsConstructor;
import pl.archala.domain.balance.BalanceGeneratedIdentifier;
import pl.archala.domain.balance.GenerateBalanceIdentifier;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class BalanceIdentifierGenerator implements GenerateBalanceIdentifier {

    private final SecureRandom secureRandom;

    private static final int DEFAULT_LENGTH = 20;

    /*
    Generator should check if generated balance id is not already present in the DB
    but 20-digits password can have 10^20 combinations, so for this demo app it is
    enough, and we do not have to worry about it
     */
    @Override
    public BalanceGeneratedIdentifier generate() {
        var identifier = Stream.generate(() -> secureRandom.nextInt(10))
                               .limit(DEFAULT_LENGTH)
                               .map(String::valueOf)
                               .collect(Collectors.joining());
        return new BalanceGeneratedIdentifier(identifier);
    }

}
