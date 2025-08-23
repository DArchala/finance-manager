package pl.archala.domain.balance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.archala.infrastructure.exception.ApplicationException;
import pl.archala.infrastructure.database.postgres.PostgresBalanceRepository;

@RequiredArgsConstructor
@Repository
public class BalanceRepository {

    private final PostgresBalanceRepository postgresBalanceRepository;

    public Balance persistNew(Balance balance) {
        return postgresBalanceRepository.persist(balance);
    }

    public Balance findById(String id) {
        return postgresBalanceRepository.findById(id)
                                        .orElseThrow(() -> ApplicationException.notFound("Balance with id: %s, not found".formatted(id)));
    }

    public void resetTransactionsForAllBalances() {
        postgresBalanceRepository.resetTransactionsForAllBalances();
    }

}
