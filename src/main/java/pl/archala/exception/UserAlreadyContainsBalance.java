package pl.archala.exception;

public class UserAlreadyContainsBalance extends Exception {
    public UserAlreadyContainsBalance(String message) {
        super(message);
    }
}
