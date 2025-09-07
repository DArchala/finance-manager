package pl.archala.domain.balance;

public interface BalanceRepositoryPort {

    Balance persistNew(Balance balance);

    Balance findById(String id);
}
