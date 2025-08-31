package pl.archala.application.command.balance.send_money;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.archala.application.command.user.notify.NotifyUserApplicationService;
import pl.archala.application.command.user.notify.NotifyUserSendMoneyByEmailCommand;
import pl.archala.application.command.user.notify.NotifyUserSendMoneyByPhoneCommand;
import pl.archala.domain.balance.Balance;
import pl.archala.domain.balance.BalanceRepository;
import pl.archala.domain.exception.ApplicationException;
import pl.archala.domain.user.UserRepository;
import pl.archala.shared.TransactionExecutor;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SendMoneyApplicationService {

    private final UserRepository userRepository;
    private final BalanceRepository balanceRepository;
    private final NotifyUserApplicationService notifyUserApplicationService;
    private final TransactionExecutor transactionExecutor;

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
        case EMAIL -> notifyUserApplicationService.notifyUserSendMoney(new NotifyUserSendMoneyByEmailCommand(userContractor.getEmail()));
        case SMS -> notifyUserApplicationService.notifyUserSendMoney(new NotifyUserSendMoneyByPhoneCommand(userContractor.getPhone()));
        }
    }

    private void validateSourceBalanceLimit(Balance sourceBalance) {
        if (sourceBalance.getDailyTransactionsCount() >= 3) {
            throw ApplicationException.from("Balance with id %s exceeded the daily transaction limit".formatted(sourceBalance.getId()), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


    private void validateSourceBalanceAmount(Balance sourceBalance, SendMoneyCommand command) {
        if (!sourceBalance.containsAtLeast(command.value())) {
            throw ApplicationException.from("Balance with id %s does not contain money amount to send amount: %s".formatted(sourceBalance.getId(), command.value()),
                                            HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

}
