package pl.archala.application.command.user.create;

import pl.archala.domain.notification.NotificationChannel;

public record CreateUserCommand(String username,
                                String password,
                                String phone,
                                String email,
                                NotificationChannel notificationChannel) {

}
