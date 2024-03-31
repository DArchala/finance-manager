package pl.archala.exception;

public class UserAlreadyContainsBalanceException extends Exception {
    public UserAlreadyContainsBalanceException(String message) {
        super(message);
    }
}
