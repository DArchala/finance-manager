package pl.archala.domain.balance;

public interface BalanceRepositoryInterface {

    Balance persistNew(Balance balance);

    Balance findById(String id);
}
