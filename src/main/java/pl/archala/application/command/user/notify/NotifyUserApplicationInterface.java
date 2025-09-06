package pl.archala.application.command.user.notify;

public interface NotifyUserApplicationInterface {

    void notifyUserSendMoney(NotifyUserSendMoneyByPhoneCommand command);

    void notifyUserSendMoney(NotifyUserSendMoneyByEmailCommand command);

}
