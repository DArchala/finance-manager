package pl.archala.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.archala.entity.Balance;

@Repository
public interface BalancesRepository extends JpaRepository<Balance, Long> {

}
