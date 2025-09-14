package pl.archala.infrastructure.adapter.in.rest

import pl.archala.BaseE2ESpecification
import pl.archala.application.api.balance.RestCreateBalanceRequest
import pl.archala.application.command.balance.create.CreateBalanceResult
import pl.archala.domain.balance.Balance
import pl.archala.domain.balance.BalanceCode
import pl.archala.domain.user.User
import pl.archala.domain.user.UserFixture

class BalanceControllerE2ETest extends BaseE2ESpecification {

    def 'Should create balance for user'() {
        given: 'Prepare user'
        def currentUser = userRepository.persist(UserFixture.custom(userPasswordEncoder))

        and: 'Prepare create balance request'
        def request = new RestCreateBalanceRequest(BalanceCode.CODE_1)

        when: 'Create balance'
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
        balance.getGeneratedId() == createBalanceResult.balanceId()
        balance.getUser().getId() == currentUser.getId()
        balance.getAmount() == request.balanceCode().getValue()

        and: 'User should has assigned the balance'
        def users = userRepository.findAll()
        users.size() == 1

        def user = users.first() as User
        user.getBalance().getId() == balance.getId()
    }

    def 'Should throw exception if user already has balance'() {
        given: 'Prepare user with balance'
        def persistedUser = userRepository.persist(UserFixture.custom(userPasswordEncoder))
        def persistedBalance = balanceRepository.persist(Balance.create(generateBalanceIdentifier.generate(),
                                                                        BigDecimal.valueOf(100),
                                                                        persistedUser))

        persistedUser.updateBalance(persistedBalance)
        userRepository.update(persistedUser)

        and: 'Prepare create balance requst'
        def request = new RestCreateBalanceRequest(BalanceCode.CODE_1)

        when: 'Try to create second balance'
        webTestClient.post().uri("/api/balance")
                     .bodyValue(request)
                     .headers(headers -> headers.setBasicAuth(persistedUser.getName(), "password"))
                     .exchange()
                     .expectStatus().isBadRequest()

        then: 'Only one balance should be present in postgres'
        def balances = balanceRepository.findAll()
        balances.size() == 1

        def createdBalance = balances.first()
        verifyAll(createdBalance) {
            it.getDailyTransactionsCount() == 0
            it.getId() != null
            it.getUser().getId() == persistedUser.getId()
            it.getAmount() == request.balanceCode().getValue()
        }

        and: 'User should has assigned new balance'
        def users = userRepository.findAll()
        users.size() == 1

        def user = users.first() as User
        user.getBalance().getId() == createdBalance.getId()
    }

    def cleanup() {
        balanceRepository.deleteAll()
        userRepository.deleteAll()
    }

}
