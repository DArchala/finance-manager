package pl.archala.utils;

public final class EndpointProvider {

    private EndpointProvider() {
    }

    public static final String API_USERS = "/api/users";
    public static final String REGISTER = "/register";
    public static final String API_USERS_REGISTER = API_USERS + REGISTER;

    public static final String API_BALANCES = "/api/balances";
    public static final String TRANSACTION = "/transaction";
    public static final String API_BALANCES_TRANSACTION = API_BALANCES + TRANSACTION;

}
