package pl.archala.domain.balance


import pl.archala.domain.user.User

import java.security.SecureRandom

class BalanceFixture {

    static def random = new SecureRandom()
    static def characters = List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")

    static Balance custom(Map map = [:]) {
        def id = (map.id ?: null) as String
        def amount = (map.amount ?: BigDecimal.ZERO) as BigDecimal
        def user = (map.user ?: null) as User
        def dailyTransactionsCount = (map.dailyTransactionsCount ?: 0) as int
        return new Balance(id,
                           amount,
                           user,
                           dailyTransactionsCount)
    }

    static String generateBalanceId() {
        StringBuilder sb = new StringBuilder()
        for (int i = 0; i < 20; i++) {
            sb.append(characters.get(random.nextInt(10)))
        }
        return sb.toString()
    }

}
