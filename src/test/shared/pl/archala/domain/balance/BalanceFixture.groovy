package pl.archala.domain.balance

import pl.archala.domain.user.User

class BalanceFixture {

    static Balance custom(GenerateBalanceIdentifier generator, Map map = [:]) {
        def id = (map.id ?: null) as Long
        def generatedId = (map.id ?: generator.generate().id()) as String
        def amount = (map.amount ?: BigDecimal.ZERO) as BigDecimal
        def user = (map.user ?: null) as User
        def dailyTransactionsCount = (map.dailyTransactionsCount ?: 0) as int
        def version = (map.version ?: 0L) as Long
        return new Balance(id,
                           generatedId,
                           amount,
                           user,
                           dailyTransactionsCount,
                           version)
    }

}
