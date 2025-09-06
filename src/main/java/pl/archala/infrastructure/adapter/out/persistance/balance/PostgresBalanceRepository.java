package pl.archala.infrastructure.adapter.out.persistance.balance;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.archala.domain.balance.Balance;

import java.util.List;
import java.util.Optional;

public interface PostgresBalanceRepository extends BaseJpaRepository<Balance, String> {

    @Modifying
    @Query("update Balance b set b.dailyTransactionsCount = 0")
    void resetAllBalancesLimit();

    Optional<Balance> findBalanceByUserUsername(String userUsername);

    List<Balance> findAll();
}
