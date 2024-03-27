package pl.archala.components;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.archala.repository.BalancesRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionsScheduler {

    private final BalancesRepository balancesRepository;

    @Scheduled(cron = "${transactions-reset-scheduler-cron}")
    @Transactional
    public void reportCurrentTime() {
        log.info("All transactions limits are setting to 0...");
        balancesRepository.resetTransactionsForAllBalances();
        log.info("Transactions reset done.");
    }

}
