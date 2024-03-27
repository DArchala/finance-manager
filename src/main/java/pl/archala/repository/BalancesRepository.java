package pl.archala.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.archala.entity.Balance;

@Repository
public interface BalancesRepository extends JpaRepository<Balance, Long> {

    @Modifying
    @Query("update Balance b set b.dailyTransactionsCount = 0")
    void resetTransactionsForAllBalances();

}
