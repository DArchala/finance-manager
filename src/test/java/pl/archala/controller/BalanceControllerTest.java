package pl.archala.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.archala.PostgresqlContainer;
import pl.archala.application.rest.error.ErrorResponse;
import pl.archala.application.command.balance.create.CreateBalanceResult;
import pl.archala.application.command.user.create.CreateUserCommand;
import pl.archala.application.command.user.create.CreateUserResult;
import pl.archala.application.rest.balance.RestSendMoneyRequest;
import pl.archala.application.rest.user.RestCreateUserRequest;
import pl.archala.domain.balance.BalanceCode;
import pl.archala.domain.notification.NotificationChannel;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BalanceControllerTest extends PostgresqlContainer {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldCreateBalanceForUser() {
        //given
        BalanceCode balanceCode = BalanceCode.CODE_5;
        String username = "user123";
        String password = "passworD1@";
        RestCreateUserRequest createUserCommand = new RestCreateUserRequest(username, password, "123456789", "email@wp.pl", NotificationChannel.SMS);

        //when
        webTestClient.post().uri("/api/users/register").bodyValue(createUserCommand).exchange()
                     .expectStatus().isCreated()
                     .expectBody(CreateUserResult.class);

        CreateBalanceResult createBalanceResult = webTestClient.post().uri(uriBuilder -> uriBuilder.path("/api/balances")
                                                                                             .queryParam("code", balanceCode).build())
                                                         .headers(headers -> headers.setBasicAuth(username, password)).exchange()
                                                         .expectStatus().isCreated()
                                                         .expectBody(CreateBalanceResult.class)
                                                         .returnResult().getResponseBody();

        //then
        assertNotNull(createBalanceResult.balanceId());
        assertEquals(20, createBalanceResult.balanceId().length());
        assertEquals(BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_UP), createBalanceResult.balanceId());

    }

    @Test
    void shouldThrowExceptionIfUserWantsToCreateSecondBalance() {
        //given
        BalanceCode balanceCode = BalanceCode.CODE_5;
        String username = "user123";
        String password = "passworD1@";
        RestCreateUserRequest createUserCommand = new RestCreateUserRequest(username, password, "123456789", "email@wp.pl", NotificationChannel.SMS);
        String expectedErrorMsg = "User with name %s already contains balance, it is not possible to create next one.".formatted(username);

        //when
        webTestClient.post().uri("/api/users/register").bodyValue(createUserCommand).exchange()
                     .expectStatus().isCreated()
                     .expectBody(CreateUserResult.class);

        webTestClient.post().uri(uriBuilder -> uriBuilder.path("/api/balances")
                        .queryParam("code", balanceCode).build())
                .headers(headers -> headers.setBasicAuth(username, password)).exchange()
                .expectStatus().isCreated()
                .expectBody(CreateBalanceResult.class);

        ErrorResponse errorResponse = webTestClient.post().uri(uriBuilder -> uriBuilder.path("/api/balances")
                        .queryParam("code", balanceCode).build())
                .headers(headers -> headers.setBasicAuth(username, password)).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        //then
        assertEquals(HttpStatus.BAD_REQUEST, errorResponse.status());
        assertEquals(1, errorResponse.reasons().size());
        assertEquals(expectedErrorMsg, errorResponse.reasons().getFirst());
        assertNotNull(errorResponse.occurred());

    }

    @Test
    void shouldThrowExceptionIfUserSendMoneyWithoutCreatedBalance() {
        //given
        String username = "user111";
        String password = "passworD1@";
        RestCreateUserRequest restCreateUserRequest = new RestCreateUserRequest(username, password, "111333555", "email1@wp.pl", NotificationChannel.SMS);
        RestSendMoneyRequest restSendMoneyRequest = new RestSendMoneyRequest("1", "2", BigDecimal.valueOf(50));
        String expectedErrorMsg = "User with name %s does not have balance.".formatted(username);

        //when
        webTestClient.post().uri("/api/users/register").bodyValue(restCreateUserRequest)
                .exchange()
                .expectStatus().isCreated();

        ErrorResponse errorResponse = webTestClient.post().uri("/api/users/register").bodyValue(restSendMoneyRequest)
                .headers(headers -> headers.setBasicAuth(username, password)).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        //then
        assertEquals(1, errorResponse.reasons().size());
        assertEquals(expectedErrorMsg, errorResponse.reasons().getFirst());
        assertEquals(HttpStatus.BAD_REQUEST, errorResponse.status());
        assertNotNull(errorResponse.occurred());

    }

    @Test
    void shouldThrowExceptionIfUserWantsToSendMoneyFromNotExistingBalance() {
        //given
        BalanceCode code = BalanceCode.CODE_5;
        String notExistingBalanceId = "00000000000000000000";
        String notExistingBalanceId2 = "11111111111111111111";
        String username = "user111";
        String password = "passworD1@";
        CreateUserCommand createUserCommand1 = new CreateUserCommand(username, password, "111333555", "email1@wp.pl", NotificationChannel.SMS);
        RestSendMoneyRequest transactionDTO = new RestSendMoneyRequest(notExistingBalanceId, notExistingBalanceId2, BigDecimal.valueOf(50));
        String expectedErrorMsg = "Provided source balance with id %s does not match your balance.".formatted(notExistingBalanceId);

        //when
        webTestClient.post().uri("/api/users/register").bodyValue(createUserCommand1)
                .exchange()
                .expectStatus().isCreated();

        webTestClient.post().uri(uriBuilder -> uriBuilder.path("/api/balances")
                        .queryParam("code", code).build())
                .headers(headers -> headers.setBasicAuth(username, password)).exchange()
                .expectStatus().isCreated();

        ErrorResponse errorResponse = webTestClient.post().uri("/api/users/register").bodyValue(transactionDTO)
                .headers(headers -> headers.setBasicAuth(username, password)).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        //then
        assertEquals(1, errorResponse.reasons().size());
        assertEquals(expectedErrorMsg, errorResponse.reasons().getFirst());
        assertNotNull(errorResponse.occurred());
        assertEquals(HttpStatus.BAD_REQUEST, errorResponse.status());

    }

    @Test
    void shouldThrowExceptionIfTargetBalanceDoesNotExist() {
        //given
        BalanceCode code = BalanceCode.CODE_1;
        String notExistingTargetBalanceId = "00000000000000000000";
        String username = "user111";
        String password = "passworD1@";
        CreateUserCommand createUserCommand1 = new CreateUserCommand(username, password, "111333555", "email1@wp.pl", NotificationChannel.SMS);

        webTestClient.post().uri("/api/users/register").bodyValue(createUserCommand1)
                .exchange()
                .expectStatus().isCreated();

        CreateBalanceResult createBalanceResult = webTestClient.post().uri(uriBuilder -> uriBuilder.path("/api/balances")
                        .queryParam("code", code).build())
                .headers(headers -> headers.setBasicAuth(username, password)).exchange()
                .expectStatus().isCreated()
                .expectBody(CreateBalanceResult.class)
                .returnResult().getResponseBody();

        RestSendMoneyRequest transactionDTO = new RestSendMoneyRequest(createBalanceResult.balanceId(), notExistingTargetBalanceId, BigDecimal.valueOf(50));

        //when
        ErrorResponse errorResponse = webTestClient.post().uri("/api/users/register").bodyValue(transactionDTO)
                .headers(headers -> headers.setBasicAuth(username, password)).exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        //then
        assertEquals(1, errorResponse.reasons().size());
        assertEquals("Balance with id %s does not exist".formatted(notExistingTargetBalanceId), errorResponse.reasons().getFirst());
        assertNotNull(errorResponse.occurred());
        assertEquals(HttpStatus.NOT_FOUND, errorResponse.status());

    }

}