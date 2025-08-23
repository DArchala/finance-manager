package pl.archala.domain.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.archala.domain.notification.dto.UserNotificationData;

@Slf4j
@Service("email")
class EmailSender implements NotificationSender {

    @Override
    public void sendNotification(UserNotificationData userNotificationData, String content) {
        log.info("Sending email to email address: {}, content: {}", userNotificationData.email(), content);
    }
}
