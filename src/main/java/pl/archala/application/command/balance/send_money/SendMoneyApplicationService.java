package pl.archala.application.command.balance.send_money;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.archala.infrastructure.exception.ApplicationException;
import pl.archala.application.command.user.notify.NotifyUserApplicationService;
import pl.archala.application.command.user.notify.NotifyUserSendMoneyCommand;
import pl.archala.common.TransactionExecutor;
import pl.archala.domain.balance.BalanceRepository;
import pl.archala.domain.user.UserRepository;

@RequiredArgsConstructor
@Service
public class SendMoneyApplicationService {

    private final UserRepository userRepository;
    private final BalanceRepository balanceRepository;
    private final NotifyUserApplicationService notifyUserApplicationService;
    private final TransactionExecutor transactionExecutor;

    public void sendMoney(SendMoneyCommand command) {
        var user = userRepository.findUserByUsername(command.username());

        if (!user.hasBalanceWithId(command.sourceBalanceId())) {
            throw ApplicationException.from("Provided source balance with id: %s does not belong to: %s.".formatted(command.sourceBalanceId(), command.username()),
                                            HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var sourceBalance = balanceRepository.findById(command.sourceBalanceId());

        if (!sourceBalance.containsAtLeast(command.value())) {
            throw ApplicationException.from("Balance with id %s does not contain money amount to send amount: %s".formatted(sourceBalance.getId(), command.value()),
                                            HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (sourceBalance.getDailyTransactionsCount() >= 3) {
            throw ApplicationException.from("Balance with id %s exceeded the daily transaction limit".formatted(sourceBalance.getId()), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var targetBalance = balanceRepository.findById(command.targetBalanceId());

        transactionExecutor.executeInTransaction(() -> {
            sourceBalance.subtract(command.value());
            targetBalance.add(command.value());
            sourceBalance.incrementTransactions();
        });

        notifyUserApplicationService.notifyUserSendMoney(new NotifyUserSendMoneyCommand());
    }

}
