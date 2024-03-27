package pl.archala.dto.user;

import pl.archala.enums.NotificationChannel;

public record GetUserDTO(Long id, String username, String phone, String email, NotificationChannel notificationChannel) {

}
