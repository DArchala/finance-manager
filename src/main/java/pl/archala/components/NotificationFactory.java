package pl.archala.components;

import org.springframework.stereotype.Component;
import pl.archala.dto.user.UserNotificationData;
import pl.archala.enums.NotificationChannel;
import pl.archala.exception.UnsupportedNotificationTypeException;
import pl.archala.service.senders.NotificationSender;

import java.math.BigDecimal;
import java.util.Map;

import static pl.archala.utils.ExceptionInfoProvider.UNSUPPORTED_NOTIFICATION_TYPE;

@Component
public class NotificationFactory {

    private final Map<String, NotificationSender> notificationSenderMap;

    public NotificationFactory(Map<String, NotificationSender> notificationSenders) {
        this.notificationSenderMap = notificationSenders;
    }

    public void execute(UserNotificationData userNotificationData, BigDecimal value, String targetBalanceId, BigDecimal remainingFunds) throws UnsupportedNotificationTypeException {
        NotificationSender notificationSender = getNotificationSender(userNotificationData.channel());
        String content = NotificationSender.generateContent(value, targetBalanceId, remainingFunds);
        notificationSender.sendNotification(userNotificationData, content);
    }

    private NotificationSender getNotificationSender(NotificationChannel channel) throws UnsupportedNotificationTypeException {
        NotificationSender notificationSender = notificationSenderMap.get(channel.getValue());
        if (notificationSender == null) {
            throw new UnsupportedNotificationTypeException(UNSUPPORTED_NOTIFICATION_TYPE.formatted(channel.getValue()));
        }
        return notificationSender;
    }
}
