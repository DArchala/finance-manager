package pl.archala.components;

import org.springframework.stereotype.Component;
import pl.archala.dto.user.UserNotificationData;
import pl.archala.enums.NotificationChannel;
import pl.archala.service.senders.NotificationSender;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class NotificationFactory {

    private final Map<String, NotificationSender> notificationSenderMap;

    public NotificationFactory(Map<String, NotificationSender> notificationSenders) {
        this.notificationSenderMap = notificationSenders;
    }

    public void execute(UserNotificationData userNotificationData, BigDecimal value, String targetBalanceId, BigDecimal remainingFunds) {
        NotificationSender notificationSender = getNotificationSender(userNotificationData.channel());
        String content = NotificationSender.generateContent(value, targetBalanceId, remainingFunds);
        notificationSender.sendNotification(userNotificationData, content);
    }

    private NotificationSender getNotificationSender(NotificationChannel channel) {
        NotificationSender notificationSender = notificationSenderMap.get(channel.getValue());
        if (notificationSender == null) {
            throw new RuntimeException("Unsupported notification type");
        }
        return notificationSender;
    }
}
