package pl.archala.infrastructure.adapter.out.persistence.balance;

import lombok.RequiredArgsConstructor;
import pl.archala.application.api.error.ApplicationException;
import pl.archala.application.query.find_user_balance_details.FindUserBalanceDetails;
import pl.archala.application.query.find_user_balance_details.FindUserBalanceDetailsQuery;
import pl.archala.application.query.find_user_balance_details.FindUserBalanceDetailsView;
import pl.archala.domain.balance.Balance;
import pl.archala.domain.balance.BalanceRepositoryPort;
import pl.archala.domain.balance.BalanceSchedulerPort;

@RequiredArgsConstructor
public class PostgresBalanceRepository implements FindUserBalanceDetails, BalanceRepositoryPort, BalanceSchedulerPort {

    private final JpaBalanceRepository jpaBalanceRepository;

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
        var balance = jpaBalanceRepository.findByUser_Name(query.name())
                                          .orElseThrow(() -> ApplicationException.notFound("Cannot find balance for user: %s".formatted(query.name())));

        return new FindUserBalanceDetailsView(balance.getId(),
                                              balance.getAmount(),
                                              balance.getDailyTransactionsCount());

    }
}
