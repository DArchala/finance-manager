package pl.archala.dto.user;

import pl.archala.enums.NotificationChannel;

public record GetUserDTO(String username, char[] password, String phoneNumber, String email,
                         NotificationChannel notificationChannel) {

}
