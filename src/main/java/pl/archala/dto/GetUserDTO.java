package pl.archala.dto;

import pl.archala.enums.NotificationChannel;

public record GetUserDTO(String username, char[] password, String phoneNumber, String email,
                         NotificationChannel notificationChannel) {

}
