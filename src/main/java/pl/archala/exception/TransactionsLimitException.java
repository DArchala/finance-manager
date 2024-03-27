package pl.archala.exception;

public class TransactionsLimitException extends Exception {
    public TransactionsLimitException(String message) {
        super(message);
    }
}
