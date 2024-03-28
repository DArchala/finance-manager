package pl.archala.service.senders;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailSender implements NotificationSender {

    @Override
    public void send(String target, String content) {
        log.info("Sending email to email address: {}, content: {}", target, content);
    }
}
