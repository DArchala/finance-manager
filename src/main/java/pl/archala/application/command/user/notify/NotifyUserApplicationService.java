package pl.archala.application.command.user.notify;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotifyUserApplicationService {

    @Async("singleThreadTaskExecutor")
    public void notifyUserSendMoney(NotifyUserSendMoneyByPhoneCommand command) {
        log.info("[NOTIFICATION] Money has been sent, notification for phone number: {}", command.phoneNumber());
    }

    @Async("singleThreadTaskExecutor")
    public void notifyUserSendMoney(NotifyUserSendMoneyByEmailCommand command) {
        log.info("[NOTIFICATION] Money has been sent, notification for email: {}", command.email());
    }

}
