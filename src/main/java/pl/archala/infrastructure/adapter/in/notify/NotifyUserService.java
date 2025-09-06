package pl.archala.infrastructure.adapter.in.notify;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import pl.archala.application.command.user.notify.NotifyUserApplicationInterface;
import pl.archala.application.command.user.notify.NotifyUserSendMoneyByEmailCommand;
import pl.archala.application.command.user.notify.NotifyUserSendMoneyByPhoneCommand;

@Slf4j
public class NotifyUserService implements NotifyUserApplicationInterface {

    @Async("singleThreadTaskExecutor")
    public void notifyUserSendMoney(NotifyUserSendMoneyByPhoneCommand command) {
        log.info("[NOTIFICATION] Money has been sent, notification for phone number: {}", command.phoneNumber());
    }

    @Async("singleThreadTaskExecutor")
    public void notifyUserSendMoney(NotifyUserSendMoneyByEmailCommand command) {
        log.info("[NOTIFICATION] Money has been sent, notification for email: {}", command.email());
    }
}
