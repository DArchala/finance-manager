package pl.archala.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.archala.FinanceManagerApplicationTests;
import pl.archala.PostgresqlContainer;
import pl.archala.dto.balance.GetBalanceDTO;
import pl.archala.dto.errorResponse.ErrorResponse;
import pl.archala.dto.user.AddUserDTO;
import pl.archala.dto.user.GetUserDTO;
import pl.archala.enums.BalanceCode;
import pl.archala.enums.NotificationChannel;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {FinanceManagerApplicationTests.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BalancesControllerTest extends PostgresqlContainer {

    @Autowired
    private WebTestClient rest;

    @Test
    void shouldCreateBalanceForUser() {
        //given
        BalanceCode balanceCode = BalanceCode.KOD_5;
        String username = "user123";
        String password = "passworD1@";
        AddUserDTO addUserDTO = new AddUserDTO(username, password, "123456789", "email@wp.pl", NotificationChannel.SMS);

        //when
        rest.post().uri("/api/users/register").bodyValue(addUserDTO).exchange()
                .expectStatus().isCreated()
                .expectBody(GetUserDTO.class);

        GetBalanceDTO getBalanceDTO = rest.post().uri(uriBuilder -> uriBuilder.path("/api/balances")
                        .queryParam("code", balanceCode).build())
                .headers(headers -> headers.setBasicAuth(username, password)).exchange()
                .expectStatus().isCreated()
                .expectBody(GetBalanceDTO.class)
                .returnResult().getResponseBody();

        //then
        assertEquals("1", getBalanceDTO.id());
        assertEquals(BigDecimal.valueOf(500), getBalanceDTO.value());

    }

    @Test
    void shouldThrowExceptionIfUserWantsToCreateSecondBalance() {
        //given
        BalanceCode balanceCode = BalanceCode.KOD_5;
        String username = "user123";
        String password = "passworD1@";
        AddUserDTO addUserDTO = new AddUserDTO(username, password, "123456789", "email@wp.pl", NotificationChannel.SMS);
        String expectedErrorMsg = "User %s already contains balance, it is not possible to create next one.".formatted(username);

        //when
        rest.post().uri("/api/users/register").bodyValue(addUserDTO).exchange()
                .expectStatus().isCreated()
                .expectBody(GetUserDTO.class);

        rest.post().uri(uriBuilder -> uriBuilder.path("/api/balances")
                        .queryParam("code", balanceCode).build())
                .headers(headers -> headers.setBasicAuth(username, password)).exchange()
                .expectStatus().isCreated()
                .expectBody(GetBalanceDTO.class);

        ErrorResponse errorResponse = rest.post().uri(uriBuilder -> uriBuilder.path("/api/balances")
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
        AddUserDTO addUserDTO1 = new AddUserDTO(username, password, "111333555", "email1@wp.pl", NotificationChannel.SMS);
        String expectedErrorMsg = "User %s does not have balance.".formatted(username);

        //when
        rest.post().uri("/api/users/register").bodyValue(addUserDTO1)
                .exchange()
                .expectStatus().isCreated();

        ErrorResponse errorResponse = rest.post().uri(uriBuilder -> uriBuilder.path("/api/balances/transaction")
                        .queryParam("sourceBalanceId", "1")
                        .queryParam("targetBalanceId", "2")
                        .queryParam("value", BigDecimal.valueOf(50))
                        .build())
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

}