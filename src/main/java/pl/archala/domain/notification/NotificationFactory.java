package pl.archala.domain.notification;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.archala.domain.notification.dto.UserNotificationData;
import pl.archala.domain.exception.ApplicationException;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class NotificationFactory {

    private final Map<String, NotificationSender> notificationSenderMap;

    public NotificationFactory(Map<String, NotificationSender> notificationSenders) {
        this.notificationSenderMap = notificationSenders;
    }

    public void execute(UserNotificationData userNotificationData, BigDecimal value, String targetBalanceId, BigDecimal remainingFunds) {
        NotificationSender notificationSender = getNotificationSender(userNotificationData.notificationChannel());
        String content = NotificationSender.generateContent(value, targetBalanceId, remainingFunds);
        notificationSender.sendNotification(userNotificationData, content);
    }

    private NotificationSender getNotificationSender(NotificationChannel channel) {
        NotificationSender notificationSender = notificationSenderMap.get(channel.getValue());
        if (notificationSender == null) {
            throw ApplicationException.from("Unsupported notification type: %s".formatted(channel.getValue()), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return notificationSender;
    }
}
