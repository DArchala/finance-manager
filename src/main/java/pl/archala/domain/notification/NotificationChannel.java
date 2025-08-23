package pl.archala.domain.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationChannel {
    SMS("sms"), EMAIL("email");

    private final String value;

}
