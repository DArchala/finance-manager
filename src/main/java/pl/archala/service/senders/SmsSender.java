package pl.archala.service.senders;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.archala.dto.user.UserNotificationData;

@Slf4j
@Service("sms")
public class SmsSender implements NotificationSender {

    @Override
    public void sendNotification(UserNotificationData userNotificationData, String content) {
        log.info("Sending sms to phone number: {}, content: {}", userNotificationData.phone(), content);
    }

}
