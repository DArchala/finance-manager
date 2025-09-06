package pl.archala.infrastructure.adapter.out.persistance.balance;

import pl.archala.application.query.find_user_balance_details.FindUserBalanceDetails;
import pl.archala.application.query.find_user_balance_details.FindUserBalanceDetailsQuery;
import pl.archala.application.query.find_user_balance_details.FindUserBalanceDetailsView;
import pl.archala.domain.balance.Balance;
import pl.archala.domain.balance.BalanceRepositoryInterface;
import pl.archala.application.api.error.ApplicationException;

public record BalanceRepository(PostgresBalanceRepository postgresBalanceRepository) implements FindUserBalanceDetails, BalanceRepositoryInterface {

    @Override
    public Balance persistNew(Balance balance) {
        return postgresBalanceRepository.persist(balance);
    }

    @Override
    public Balance findById(String id) {
        return postgresBalanceRepository.findById(id)
                                        .orElseThrow(() -> ApplicationException.notFound("Balance with id: %s, not found".formatted(id)));
    }

    public void resetTransactionsForAllBalances() {
        postgresBalanceRepository.resetAllBalancesLimit();
    }

    @Override
    public FindUserBalanceDetailsView findUserBalanceDetails(FindUserBalanceDetailsQuery query) {
        var balance = postgresBalanceRepository.findBalanceByUserUsername(query.username())
                                               .orElseThrow(() -> ApplicationException.notFound("Cannot find balance for user: %s".formatted(query.username())));

        return new FindUserBalanceDetailsView(balance.getId(),
                                              balance.getAmount(),
                                              balance.getDailyTransactionsCount());

    }
}
