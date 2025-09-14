package pl.archala.application.command.balance.send_money;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.archala.application.api.error.ApplicationException;
import pl.archala.application.api.error.ErrorCode;
import pl.archala.application.command.user.notify.NotifyUser;
import pl.archala.application.command.user.notify.NotifyUserSendMoneyByEmailCommand;
import pl.archala.application.command.user.notify.NotifyUserSendMoneyByPhoneCommand;
import pl.archala.domain.balance.Balance;
import pl.archala.domain.balance.BalanceRepositoryPort;
import pl.archala.domain.user.UserRepositoryPort;
import pl.archala.shared.TransactionExecutor;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class SendMoneyApplicationService {

    private final UserRepositoryPort userRepositoryPort;
    private final BalanceRepositoryPort balanceRepositoryPort;
    private final NotifyUser notifyUser;
    private final TransactionExecutor transactionExecutor;

    public void sendMoney(SendMoneyCommand command) {
        var userContractor = userRepositoryPort.findByName(command.username());
        var sourceBalance = Optional.ofNullable(userContractor.getBalance())
                                    .orElseThrow(() -> ApplicationException.notFound("User with name: %s, does not have balance".formatted(command.username())));

        validateSourceBalanceAmount(sourceBalance, command.amount());
        validateSourceBalanceLimit(sourceBalance);

        var targetBalance = balanceRepositoryPort.findById(command.targetBalanceId());

        transactionExecutor.executeInTransaction(() -> {
            sourceBalance.subtract(command.amount());
            targetBalance.add(command.amount());
            sourceBalance.incrementTransactions();
        });

        switch (userContractor.getNotificationChannel()) {
        case EMAIL -> notifyUser.notifyUserSendMoney(new NotifyUserSendMoneyByEmailCommand(userContractor.getEmail()));
        case SMS -> notifyUser.notifyUserSendMoney(new NotifyUserSendMoneyByPhoneCommand(userContractor.getPhone()));
        }
    }

    private void validateSourceBalanceLimit(Balance sourceBalance) {
        if (sourceBalance.getDailyTransactionsCount() >= 3) {
            throw ApplicationException.from("Balance with id %s exceeded the daily transaction limit".formatted(sourceBalance.getId()), ErrorCode.UNPROCESSABLE_ENTITY);
        }
    }

    private void validateSourceBalanceAmount(Balance sourceBalance, BigDecimal amount) {
        if (!sourceBalance.containsAtLeast(amount)) {
            throw ApplicationException.from("Balance with id %s does not contain money amount to send amount: %s".formatted(sourceBalance.getId(), amount),
                                            ErrorCode.UNPROCESSABLE_ENTITY);
        }
    }

}
