package pl.archala.infrastructure.adapter.out.rest


import pl.archala.BaseE2ESpecification
import pl.archala.application.api.balance.RestCreateBalanceRequest
import pl.archala.application.command.balance.create.CreateBalanceResult
import pl.archala.domain.balance.Balance
import pl.archala.domain.balance.BalanceCode
import pl.archala.domain.balance.BalanceFixture
import pl.archala.domain.user.User
import pl.archala.domain.user.UserFixture

class BalanceControllerTest extends BaseE2ESpecification {

    def 'Should create balance for user'() {
        given:
        def currentUser = userRepository.persist(UserFixture.custom(userPasswordEncoder))

        def request = new RestCreateBalanceRequest(BalanceCode.CODE_1)

        when:
        def createBalanceResult = webTestClient.post().uri("/api/balance")
                                .bodyValue(request)
                                .headers(headers -> headers.setBasicAuth(currentUser.getName(), "password"))
                                .exchange()
                                .expectStatus().isCreated()
                                .expectBody(CreateBalanceResult.class)
                                .returnResult().getResponseBody()

        then: 'Should be created one balance'
        def balances = balanceRepository.findAll()
        balances.size() == 1

        def balance = balances.first() as Balance
        balance.getDailyTransactionsCount() == 0
        balance.getId() == createBalanceResult.balanceId()
        balance.getUser().getId() == currentUser.getId()
        balance.getAmount() == request.balanceCode().getValue()

        and: 'User should has assigned new balance'
        def users = userRepository.findAll()
        users.size() == 1

        def user = users.first() as User
        user.getBalance().getId() == balance.getId()
    }

    def 'Should throw exception if user already has balance'() {
        given:
        def currentUser = userRepository.persistAndFlush(UserFixture.custom(userPasswordEncoder))
        def currentBalance = balanceRepository.persistAndFlush(BalanceFixture.custom(generateBalanceIdentifier))

        currentBalance.updateUser(currentUser)
        currentUser.updateBalance(currentBalance)

        userRepository.flush()

        def request = new RestCreateBalanceRequest(BalanceCode.CODE_1)

        when:
        def createBalanceResult = webTestClient.post().uri("/api/balance")
                                .bodyValue(request)
                                .headers(headers -> headers.setBasicAuth(currentUser.getName(), "password"))
                                .exchange()
                                .expectStatus().isCreated()
                                .expectBody(CreateBalanceResult.class)
                                .returnResult().getResponseBody()

        then: 'Should be created one balance'
        def balances = balanceRepository.findAll()
        balances.size() == 1

        def balance = balances.first() as Balance
        balance.getDailyTransactionsCount() == 0
        balance.getId() == createBalanceResult.balanceId()
        balance.getUser().getId() == currentUser.getId()
        balance.getAmount() == request.balanceCode().getValue()

        and: 'User should has assigned new balance'
        def users = userRepository.findAll()
        users.size() == 1

        def user = users.first() as User
        user.getBalance().getId() == balance.getId()
    }

    def cleanup() {
        balanceRepository.deleteAll()
        userRepository.deleteAll()
    }

}
