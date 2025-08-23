package pl.archala.infrastructure.database.postgres;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.archala.domain.balance.Balance;

public interface PostgresBalanceRepository extends BaseJpaRepository<Balance, String> {

    @Modifying
    @Query("update Balance b set b.dailyTransactionsCount = 0")
    void resetTransactionsForAllBalances();

}
