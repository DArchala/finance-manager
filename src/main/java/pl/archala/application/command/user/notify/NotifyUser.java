package pl.archala.application.command.user.notify;

public interface NotifyUser {

    void notifyUserSendMoney(NotifyUserSendMoneyByPhoneCommand command);

    void notifyUserSendMoney(NotifyUserSendMoneyByEmailCommand command);

}
