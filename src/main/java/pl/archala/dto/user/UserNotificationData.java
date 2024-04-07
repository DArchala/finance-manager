package pl.archala.dto.user;

import pl.archala.enums.NotificationChannel;

public record UserNotificationData(NotificationChannel notificationChannel, String phone, String email) {
}
