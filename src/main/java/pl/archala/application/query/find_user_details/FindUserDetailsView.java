package pl.archala.application.query.find_user_details;

import pl.archala.domain.notification.NotificationChannel;

public record FindUserDetailsView(String name,
                                  String phone,
                                  String email,
                                  NotificationChannel notificationChannel) {
}
