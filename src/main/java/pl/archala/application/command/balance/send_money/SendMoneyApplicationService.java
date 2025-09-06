package pl.archala.application.command.balance.send_money;

import lombok.extern.slf4j.Slf4j;
import pl.archala.application.api.error.ApplicationException;
import pl.archala.application.api.error.ErrorCode;
import pl.archala.application.command.user.notify.NotifyUserApplicationInterface;
import pl.archala.application.command.user.notify.NotifyUserSendMoneyByEmailCommand;
import pl.archala.application.command.user.notify.NotifyUserSendMoneyByPhoneCommand;
import pl.archala.domain.balance.Balance;
import pl.archala.domain.balance.BalanceRepositoryInterface;
import pl.archala.domain.user.UserRepositoryInterface;
import pl.archala.shared.TransactionExecutor;

import java.util.Optional;

@Slf4j
public record SendMoneyApplicationService(UserRepositoryInterface userRepository,
                                          BalanceRepositoryInterface balanceRepository,
                                          NotifyUserApplicationInterface notifyUserApplicationInterface,
                                          TransactionExecutor transactionExecutor) {

    public void sendMoney(SendMoneyCommand command) {
        var userContractor = userRepository.findUserByUsername(command.username());
        var sourceBalance = Optional.ofNullable(userContractor.getBalance())
                                    .orElseThrow(() -> ApplicationException.notFound("User with username: %s, does not have balance".formatted(command.username())));

        validateSourceBalanceAmount(sourceBalance, command);
        validateSourceBalanceLimit(sourceBalance);

        var targetBalance = balanceRepository.findById(command.targetBalanceId());

        transactionExecutor.executeInTransaction(() -> {
            sourceBalance.subtract(command.value());
            targetBalance.add(command.value());
            sourceBalance.incrementTransactions();
        });

        switch (userContractor.getNotificationChannel()) {
        case EMAIL -> notifyUserApplicationInterface.notifyUserSendMoney(new NotifyUserSendMoneyByEmailCommand(userContractor.getEmail()));
        case SMS -> notifyUserApplicationInterface.notifyUserSendMoney(new NotifyUserSendMoneyByPhoneCommand(userContractor.getPhone()));
        }
    }

    private void validateSourceBalanceLimit(Balance sourceBalance) {
        if (sourceBalance.getDailyTransactionsCount() >= 3) {
            throw ApplicationException.from("Balance with id %s exceeded the daily transaction limit".formatted(sourceBalance.getId()), ErrorCode.UNPROCESSABLE_ENTITY);
        }
    }

    private void validateSourceBalanceAmount(Balance sourceBalance, SendMoneyCommand command) {
        if (!sourceBalance.containsAtLeast(command.value())) {
            throw ApplicationException.from("Balance with id %s does not contain money amount to send amount: %s".formatted(sourceBalance.getId(), command.value()),
                                            ErrorCode.UNPROCESSABLE_ENTITY);
        }
    }

}
