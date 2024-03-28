package pl.archala.service.senders;

public interface NotificationSender {

    void send(String target, String content);
}
