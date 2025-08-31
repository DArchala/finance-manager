package pl.archala.application.command.user.notify;

public record NotifyUserSendMoneyByPhoneCommand(String phoneNumber) {
}

/*

Po udanym przelewie powinna zostać wysłana asynchronicznie(w innym wątku) wiadomość z użyciem preferowanego kanału zapisanego
przy użytkownik. Wysyłka SMS oraz email powinna być zaślepiona i w przypadku wysyłki jednym z tych dwóch kanałów powinien pojawić
się log:

SMS: "sending sms to phone number: {phoneNumber}, {content}"

E-mail: "sending email to email addres: {email}, content: {content} "

 */