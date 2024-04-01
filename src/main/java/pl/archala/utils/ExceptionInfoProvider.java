package pl.archala.utils;

public final class ExceptionInfoProvider {

    public static final String USER_ALREADY_CONTAINS_BALANCE = "User with name %s already contains balance, it is not possible to create next one.";
    public static final String USER_WITH_USERNAME_DOES_NOT_EXIST = "User with name %s does not exist";
    public static final String BALANCE_WITH_ID_DOES_NOT_EXIST = "Balance with id %s does not exist";
    public static final String INSUFFICIENT_FUNDS = "Current balance with id %s has lower value than defined value to send";
    public static final String USERNAME_IS_ALREADY_TAKEN = "Provided username %s is already taken";
    public static final String EMAIL_IS_ALREADY_TAKEN = "Provided email %s is already taken";
    public static final String PHONE_IS_ALREADY_TAKEN = "Provided phone %s is already taken";
    public static final String TRANSACTIONS_LIMIT_EXCEEDED = "Balance with id %s exceeded the daily transaction limit";
    public static final String INVALID_SOURCE_BALANCE = "Provided source balance with id %s does not match your balance.";
    public static final String USER_DOES_NOT_HAVE_BALANCE = "User with name %s does not have balance.";

}
