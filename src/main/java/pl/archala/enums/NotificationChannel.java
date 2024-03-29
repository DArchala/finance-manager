package pl.archala.enums;

import lombok.Getter;

@Getter
public enum NotificationChannel {
    SMS("sms"), EMAIL("email");

    private final String value;

    NotificationChannel(String value) {
        this.value = value;
    }
}
