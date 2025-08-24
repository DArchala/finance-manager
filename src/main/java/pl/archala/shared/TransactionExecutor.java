package pl.archala.shared;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class TransactionExecutor {

    @Transactional
    public void executeInTransaction(Runnable runnable) {
        runnable.run();
    }

    @Transactional
    public <T> T executeInTransactionAndReturn(Supplier<T> supplier) {
        return supplier.get();
    }

}
