package pl.archala.infrastructure.adapter.out.persistence.balance;

import pl.archala.application.api.error.ApplicationException;
import pl.archala.application.query.find_user_balance_details.FindUserBalanceDetails;
import pl.archala.application.query.find_user_balance_details.FindUserBalanceDetailsQuery;
import pl.archala.application.query.find_user_balance_details.FindUserBalanceDetailsView;
import pl.archala.domain.balance.Balance;
import pl.archala.domain.balance.BalanceRepositoryPort;
import pl.archala.domain.balance.BalanceSchedulerPort;

public record PostgresBalanceRepository(JpaBalanceRepository jpaBalanceRepository) implements FindUserBalanceDetails,
                                                                                              BalanceRepositoryPort,
                                                                                              BalanceSchedulerPort {

    @Override
    public Balance persistNew(Balance balance) {
        return jpaBalanceRepository.persist(balance);
    }

    @Override
    public Balance findById(String id) {
        return jpaBalanceRepository.findById(id)
                                   .orElseThrow(() -> ApplicationException.notFound("Balance with id: %s, not found".formatted(id)));
    }

    @Override
    public void resetTransactionsForAllBalances() {
        jpaBalanceRepository.resetAllBalancesLimit();
    }

    @Override
    public FindUserBalanceDetailsView findUserBalanceDetails(FindUserBalanceDetailsQuery query) {
        var balance = jpaBalanceRepository.findBalanceByUserUsername(query.username())
                                          .orElseThrow(() -> ApplicationException.notFound("Cannot find balance for user: %s".formatted(query.username())));

        return new FindUserBalanceDetailsView(balance.getId(),
                                              balance.getAmount(),
                                              balance.getDailyTransactionsCount());

    }
}
