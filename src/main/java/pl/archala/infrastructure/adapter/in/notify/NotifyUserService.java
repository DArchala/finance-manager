package pl.archala.infrastructure.adapter.in.notify;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import pl.archala.application.command.user.notify.NotifyUser;
import pl.archala.application.command.user.notify.NotifyUserSendMoneyByEmailCommand;
import pl.archala.application.command.user.notify.NotifyUserSendMoneyByPhoneCommand;

import static pl.archala.infrastructure.config.async.AsyncConfiguration.SINGLE_THREAD_EXECUTOR_BEAN;

@Slf4j
public class NotifyUserService implements NotifyUser {

    @Async(SINGLE_THREAD_EXECUTOR_BEAN)
    public void notifyUserSendMoney(NotifyUserSendMoneyByPhoneCommand command) {
        log.info("[NOTIFICATION] Money has been sent, notification for phone number: {}", command.phoneNumber());
    }

    @Async(SINGLE_THREAD_EXECUTOR_BEAN)
    public void notifyUserSendMoney(NotifyUserSendMoneyByEmailCommand command) {
        log.info("[NOTIFICATION] Money has been sent, notification for email: {}", command.email());
    }
}
