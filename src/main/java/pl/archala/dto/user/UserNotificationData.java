package pl.archala.dto.user;

import pl.archala.enums.NotificationChannel;

public record UserNotificationData(NotificationChannel channel, String phone, String email) {
}
