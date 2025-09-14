package pl.archala.infrastructure.adapter.out.persistence.balance;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.jetbrains.annotations.TestOnly;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.archala.domain.balance.Balance;

import java.util.List;
import java.util.Optional;

public interface JpaBalanceRepository extends BaseJpaRepository<Balance, String> {

    @Modifying
    @Query("update Balance b set b.dailyTransactionsCount = 0")
    void resetAllBalancesLimit();

    Optional<Balance> findByUser_Name(String userName);

    List<Balance> findAll();

    @TestOnly
    void deleteAll();
}
