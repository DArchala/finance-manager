package pl.archala.dto.user;

import pl.archala.enums.NotificationChannel;

public record GetUserDTO(String username, String password, String phoneNumber, String email,
                         NotificationChannel notificationChannel) {

}
