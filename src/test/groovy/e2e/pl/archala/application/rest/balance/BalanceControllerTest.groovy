package pl.archala.application.rest.balance


import pl.archala.BaseE2ESpecification
import pl.archala.application.command.balance.create.CreateBalanceResult
import pl.archala.domain.balance.Balance
import pl.archala.domain.balance.BalanceCode
import pl.archala.domain.user.UserFixture

class BalanceControllerTest extends BaseE2ESpecification {

    def 'Should create balance for user'() {
        given:
        def user = postgresUserRepository.persist(UserFixture.custom(passwordEncoder))

        def request = new RestCreateBalanceRequest(BalanceCode.CODE_1)

        when:
        def body = webTestClient.post().uri("/api/balance")
                                .bodyValue(request)
                                .headers(headers -> headers.setBasicAuth(user.getUsername(), "password"))
                                .exchange()
                                .expectStatus().isCreated()
                                .expectBody(CreateBalanceResult.class)
                                .returnResult().getResponseBody()

        then:
        def balances = postgresBalanceRepository.findAll()
        balances.size() == 1

        def balance = balances.first() as Balance
        balance.getDailyTransactionsCount() == 0
        balance.getId() != null
        balance.getUser().getId() == user.getId()
        balance.getValue() == request.balanceCode().getValue()

        body.balanceId() == balance.getId()
    }

}
