package pl.archala.service.senders;

import pl.archala.dto.user.UserNotificationData;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface NotificationSender {

    static String generateContent(BigDecimal value, String targetBalanceId, BigDecimal remainingFunds) {
        return "Transaction has been sent, value = %s, target balance id = %s, dateTime = %s, remaining funds = %s".formatted(value, targetBalanceId, LocalDateTime.now(), remainingFunds);
    }

    void sendNotification(UserNotificationData userNotificationData, String content);
}
