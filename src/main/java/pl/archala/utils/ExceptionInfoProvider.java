package pl.archala.utils;

public final class ExceptionInfoProvider {

    private ExceptionInfoProvider() {}

    private static final String USER_ALREADY_CONTAINS_BALANCE = "User with name %s already contains balance, it is not possible to create next one.";
    private static final String USER_WITH_USERNAME_DOES_NOT_EXIST = "User with name %s does not exist";
    private static final String BALANCE_WITH_ID_DOES_NOT_EXIST = "Balance with id %s does not exist";
    private static final String INSUFFICIENT_FUNDS = "Current balance with id %s has lower value than defined value to send";
    private static final String USERNAME_IS_ALREADY_TAKEN = "Provided username %s is already taken";
    private static final String EMAIL_IS_ALREADY_TAKEN = "Provided email %s is already taken";
    private static final String PHONE_IS_ALREADY_TAKEN = "Provided phone %s is already taken";
    private static final String TRANSACTIONS_LIMIT_EXCEEDED = "Balance with id %s exceeded the daily transaction limit";
    private static final String INVALID_SOURCE_BALANCE = "Provided source balance with id %s does not match your balance.";
    private static final String USER_DOES_NOT_HAVE_BALANCE = "User with name %s does not have balance.";
    private static final String UNSUPPORTED_NOTIFICATION_TYPE = "Unsupported notification type: %s";

    public static String userAlreadyContainsBalance(final String username) {
        return USER_ALREADY_CONTAINS_BALANCE.formatted(username);
    }

    public static String userWithUsernameDoesNotExist(final String username) {
        return USER_WITH_USERNAME_DOES_NOT_EXIST.formatted(username);
    }

    public static String balanceWithIdDoesNotExist(final String balanceId) {
        return BALANCE_WITH_ID_DOES_NOT_EXIST.formatted(balanceId);
    }

    public static String insufficientFunds(final String balanceId) {
        return INSUFFICIENT_FUNDS.formatted(balanceId);
    }

    public static String usernameIsAlreadyTaken(final String username) {
        return USERNAME_IS_ALREADY_TAKEN.formatted(username);
    }

    public static String emailIsAlreadyTaken(final String email) {
        return EMAIL_IS_ALREADY_TAKEN.formatted(email);
    }

    public static String phoneIsAlreadyTaken(final String phone) {
        return PHONE_IS_ALREADY_TAKEN.formatted(phone);
    }

    public static String transactionsLimitExceeded(final String balanceId) {
        return TRANSACTIONS_LIMIT_EXCEEDED.formatted(balanceId);
    }

    public static String invalidSourceBalance(final String balanceId) {
        return INVALID_SOURCE_BALANCE.formatted(balanceId);
    }

    public static String userDoesNotHaveBalance(final String username) {
        return USER_DOES_NOT_HAVE_BALANCE.formatted(username);
    }

    public static String unsupportedNotificationType(final String notificationType) {
        return UNSUPPORTED_NOTIFICATION_TYPE.formatted(notificationType);
    }

}
