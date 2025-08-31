package pl.archala.application.command.user.notify;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.archala.domain.exception.ApplicationException;

@Slf4j
@Service
public class NotifyUserApplicationService {

    @Async("singleThreadTaskExecutor")
    public void notifyUserSendMoney(NotifyUserSendMoneyByPhoneCommand command) {
        try {
            for (int i = 0; i < 10; i++) {
                log.info("Powiadomienie SMS zostanie wysłane za :" + (10-i));
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            throw ApplicationException.from("Interrupted exception: %s".formatted(e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("Money has been sent, phone number: {}", command.phoneNumber());
    }

    @Async("singleThreadTaskExecutor")
    public void notifyUserSendMoney(NotifyUserSendMoneyByEmailCommand command) {
        try {
            for (int i = 0; i < 10; i++) {
                log.info("Powiadomienie EMAIL zostanie wysłane za {}...", (10-i));
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            throw ApplicationException.from("Interrupted exception: %s".formatted(e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("Money has been sent, email: {}", command.email());
    }

}
