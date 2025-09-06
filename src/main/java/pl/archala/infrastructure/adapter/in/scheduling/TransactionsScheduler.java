package pl.archala.infrastructure.adapter.in.scheduling;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import pl.archala.infrastructure.adapter.out.persistance.balance.BalanceRepository;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
@EnableScheduling
public class TransactionsScheduler {

    private final BalanceRepository balancesRepository;

    @Scheduled(cron = "${transactions-reset-scheduler-cron}")
    @Transactional
    public void resetTransactionsLimits() {
        log.info("All transactions limits are resetting to 0...");
        balancesRepository.resetTransactionsForAllBalances();
        log.info("Transactions limits reset done {}", Instant.now());
    }

}
