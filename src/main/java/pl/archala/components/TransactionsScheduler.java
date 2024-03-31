package pl.archala.components;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.archala.repository.BalancesRepository;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableScheduling
public class TransactionsScheduler {

    private final BalancesRepository balancesRepository;

    @Scheduled(cron = "${transactions-reset-scheduler-cron}")
    @Transactional
    public void resetTransactionsLimits() {
        log.info("All transactions limits are resetting to 0...");
        balancesRepository.resetTransactionsForAllBalances();
        log.info("Transactions limits reset done {}", LocalDateTime.now());
    }

}
