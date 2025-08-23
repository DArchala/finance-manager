package pl.archala.domain.notification;

import pl.archala.domain.notification.dto.UserNotificationData;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface NotificationSender {

    static String generateContent(BigDecimal value, String targetBalanceId, BigDecimal remainingFunds) {
        return "Transaction has been sent, amount = %s, target balance id = %s, dateTime = %s, remaining funds = %s".formatted(value, targetBalanceId, LocalDateTime.now(), remainingFunds);
    }

    void sendNotification(UserNotificationData userNotificationData, String content);
}
