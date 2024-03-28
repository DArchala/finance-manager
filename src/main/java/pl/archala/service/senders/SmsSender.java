package pl.archala.service.senders;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SmsSender implements NotificationSender {

    @Override
    public void send(String target, String content) {
        log.info("Sending sms to phone number: {}, content: {}", target, content);
    }

}
