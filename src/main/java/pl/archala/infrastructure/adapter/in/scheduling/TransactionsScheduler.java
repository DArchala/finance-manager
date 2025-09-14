package pl.archala.infrastructure.adapter.in.scheduling;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import pl.archala.domain.balance.BalanceSchedulerPort;

@Slf4j
@RequiredArgsConstructor
@EnableScheduling
public class TransactionsScheduler {

    private final BalanceSchedulerPort balanceSchedulerPort;

    @Scheduled(cron = "${transactions-reset-scheduler-cron}")
    @Transactional
    public void resetTransactionsLimits() {
        log.info("All transactions limits are resetting to 0...");
        balanceSchedulerPort.resetTransactionsForAllBalances();
        log.info("Transactions limits reset finished.");
    }

}
