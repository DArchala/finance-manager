package pl.archala.domain.notification.dto;

import pl.archala.domain.notification.NotificationChannel;

public record UserNotificationData(NotificationChannel notificationChannel, String phone, String email) {
}
