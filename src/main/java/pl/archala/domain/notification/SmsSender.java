package pl.archala.domain.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.archala.domain.notification.dto.UserNotificationData;

@Slf4j
@Service("sms")
class SmsSender implements NotificationSender {

    @Override
    public void sendNotification(UserNotificationData userNotificationData, String content) {
        log.info("Sending sms to phone number: {}, content: {}", userNotificationData.phone(), content);
    }

}
