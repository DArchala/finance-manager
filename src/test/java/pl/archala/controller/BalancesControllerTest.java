package pl.archala.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.archala.FinanceManagerApplicationTests;
import pl.archala.PostgresqlContainer;
import pl.archala.dto.balance.BalanceTransactionDTO;
import pl.archala.dto.balance.GetBalanceDTO;
import pl.archala.dto.errorResponse.ErrorResponse;
import pl.archala.dto.user.AddUserDTO;
import pl.archala.dto.user.GetUserDTO;
import pl.archala.enums.BalanceCode;
import pl.archala.enums.NotificationChannel;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;
import static pl.archala.utils.ExceptionInfoProvider.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {FinanceManagerApplicationTests.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BalancesControllerTest extends PostgresqlContainer {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldCreateBalanceForUser() {
        //given
        BalanceCode balanceCode = BalanceCode.KOD_5;
        String username = "user123";
        String password = "passworD1@";
        AddUserDTO addUserDTO = new AddUserDTO(username, password, "123456789", "email@wp.pl", NotificationChannel.SMS);

        //when
        webTestClient.post().uri("/api/users/register").bodyValue(addUserDTO).exchange()
                .expectStatus().isCreated()
                .expectBody(GetUserDTO.class);

        GetBalanceDTO getBalanceDTO = webTestClient.post().uri(uriBuilder -> uriBuilder.path("/api/balances")
                        .queryParam("code", balanceCode).build())
                .headers(headers -> headers.setBasicAuth(username, password)).exchange()
                .expectStatus().isCreated()
                .expectBody(GetBalanceDTO.class)
                .returnResult().getResponseBody();

        //then
        assertNotNull(getBalanceDTO.id());
        assertEquals(20, getBalanceDTO.id().length());
        assertEquals(BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_UP), getBalanceDTO.value());

    }

    @Test
    void shouldThrowExceptionIfUserWantsToCreateSecondBalance() {
        //given
        BalanceCode balanceCode = BalanceCode.KOD_5;
        String username = "user123";
        String password = "passworD1@";
        AddUserDTO addUserDTO = new AddUserDTO(username, password, "123456789", "email@wp.pl", NotificationChannel.SMS);
        String expectedErrorMsg = userAlreadyContainsBalance(username);

        //when
        webTestClient.post().uri("/api/users/register").bodyValue(addUserDTO).exchange()
                .expectStatus().isCreated()
                .expectBody(GetUserDTO.class);

        webTestClient.post().uri(uriBuilder -> uriBuilder.path("/api/balances")
                        .queryParam("code", balanceCode).build())
                .headers(headers -> headers.setBasicAuth(username, password)).exchange()
                .expectStatus().isCreated()
                .expectBody(GetBalanceDTO.class);

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
        AddUserDTO addUserDTO1 = new AddUserDTO(username, password, "111333555", "email1@wp.pl", NotificationChannel.SMS);
        BalanceTransactionDTO transactionDTO = new BalanceTransactionDTO("1", "2", BigDecimal.valueOf(50));
        String expectedErrorMsg = userDoesNotHaveBalance(username);

        //when
        webTestClient.post().uri("/api/users/register").bodyValue(addUserDTO1)
                .exchange()
                .expectStatus().isCreated();

        ErrorResponse errorResponse = webTestClient.post().uri("/api/balances/transaction").bodyValue(transactionDTO)
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
        BalanceCode code = BalanceCode.KOD_5;
        String notExistingBalanceId = "00000000000000000000";
        String notExistingBalanceId2 = "11111111111111111111";
        String username = "user111";
        String password = "passworD1@";
        AddUserDTO addUserDTO1 = new AddUserDTO(username, password, "111333555", "email1@wp.pl", NotificationChannel.SMS);
        BalanceTransactionDTO transactionDTO = new BalanceTransactionDTO(notExistingBalanceId, notExistingBalanceId2, BigDecimal.valueOf(50));
        String expectedErrorMsg = invalidSourceBalance(notExistingBalanceId);

        //when
        webTestClient.post().uri("/api/users/register").bodyValue(addUserDTO1)
                .exchange()
                .expectStatus().isCreated();

        webTestClient.post().uri(uriBuilder -> uriBuilder.path("/api/balances")
                        .queryParam("code", code).build())
                .headers(headers -> headers.setBasicAuth(username, password)).exchange()
                .expectStatus().isCreated();

        ErrorResponse errorResponse = webTestClient.post().uri("/api/balances/transaction").bodyValue(transactionDTO)
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
    void shouldThrowExceptionIfUserBalanceDoesNotContainEnoughMoney() {
        //given
        BalanceCode code = BalanceCode.KOD_1;
        String username = "user111";
        String password = "passworD1@";
        AddUserDTO addUserDTO1 = new AddUserDTO(username, password, "111333555", "email1@wp.pl", NotificationChannel.SMS);

        webTestClient.post().uri("/api/users/register").bodyValue(addUserDTO1)
                .exchange()
                .expectStatus().isCreated();

        GetBalanceDTO getBalanceDTO = webTestClient.post().uri(uriBuilder -> uriBuilder.path("/api/balances")
                        .queryParam("code", code).build())
                .headers(headers -> headers.setBasicAuth(username, password)).exchange()
                .expectStatus().isCreated()
                .expectBody(GetBalanceDTO.class)
                .returnResult().getResponseBody();

        BalanceTransactionDTO transactionDTO = new BalanceTransactionDTO(getBalanceDTO.id(), "00000000000000000000", BigDecimal.valueOf(200));

        //when
        ErrorResponse errorResponse = webTestClient.post().uri("/api/balances/transaction").bodyValue(transactionDTO)
                .headers(headers -> headers.setBasicAuth(username, password)).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        //then
        assertEquals(1, errorResponse.reasons().size());
        assertEquals(insufficientFunds(getBalanceDTO.id()), errorResponse.reasons().getFirst());
        assertNotNull(errorResponse.occurred());
        assertEquals(HttpStatus.BAD_REQUEST, errorResponse.status());

    }

    @Test
    void shouldThrowExceptionIfTargetBalanceDoesNotExist() {
        //given
        BalanceCode code = BalanceCode.KOD_1;
        String notExistingTargetBalanceId = "00000000000000000000";
        String username = "user111";
        String password = "passworD1@";
        AddUserDTO addUserDTO1 = new AddUserDTO(username, password, "111333555", "email1@wp.pl", NotificationChannel.SMS);

        webTestClient.post().uri("/api/users/register").bodyValue(addUserDTO1)
                .exchange()
                .expectStatus().isCreated();

        GetBalanceDTO getBalanceDTO = webTestClient.post().uri(uriBuilder -> uriBuilder.path("/api/balances")
                        .queryParam("code", code).build())
                .headers(headers -> headers.setBasicAuth(username, password)).exchange()
                .expectStatus().isCreated()
                .expectBody(GetBalanceDTO.class)
                .returnResult().getResponseBody();

        BalanceTransactionDTO transactionDTO = new BalanceTransactionDTO(getBalanceDTO.id(), notExistingTargetBalanceId, BigDecimal.valueOf(50));

        //when
        ErrorResponse errorResponse = webTestClient.post().uri("/api/balances/transaction").bodyValue(transactionDTO)
                .headers(headers -> headers.setBasicAuth(username, password)).exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        //then
        assertEquals(1, errorResponse.reasons().size());
        assertEquals(balanceWithIdDoesNotExist(notExistingTargetBalanceId), errorResponse.reasons().getFirst());
        assertNotNull(errorResponse.occurred());
        assertEquals(HttpStatus.NOT_FOUND, errorResponse.status());

    }

    @Test
    void shouldIncrementDailyTransactionsCountAndThrowExceptionIfWeExceedTheLimit() {
        //given
        BalanceCode code = BalanceCode.KOD_1;
        String username1 = "user111";
        String username2 = "user222";
        String password = "passworD1@";
        AddUserDTO addUserDTO1 = new AddUserDTO(username1, password, "111333555", "email1@wp.pl", NotificationChannel.SMS);
        AddUserDTO addUserDTO2 = new AddUserDTO(username2, password, "222444666", "email2@wp.pl", NotificationChannel.EMAIL);

        webTestClient.post().uri("/api/users/register").bodyValue(addUserDTO1)
                .exchange()
                .expectStatus().isCreated();

        webTestClient.post().uri("/api/users/register").bodyValue(addUserDTO2)
                .exchange()
                .expectStatus().isCreated();

        GetBalanceDTO userBalance1 = webTestClient.post().uri(uriBuilder -> uriBuilder.path("/api/balances")
                        .queryParam("code", code).build())
                .headers(headers -> headers.setBasicAuth(username1, password)).exchange()
                .expectStatus().isCreated()
                .expectBody(GetBalanceDTO.class)
                .returnResult().getResponseBody();

        GetBalanceDTO userBalance2 = webTestClient.post().uri(uriBuilder -> uriBuilder.path("/api/balances")
                        .queryParam("code", code).build())
                .headers(headers -> headers.setBasicAuth(username2, password)).exchange()
                .expectStatus().isCreated()
                .expectBody(GetBalanceDTO.class)
                .returnResult().getResponseBody();

        BalanceTransactionDTO transactionDTO = new BalanceTransactionDTO(userBalance1.id(), userBalance2.id(), BigDecimal.valueOf(10));

        //when
        GetBalanceDTO getSourceBalanceDTOAfterOneTransaction = webTestClient.post().uri("/api/balances/transaction").bodyValue(transactionDTO)
                .headers(headers -> headers.setBasicAuth(username1, password)).exchange()
                .expectStatus().isOk()
                .expectBody(GetBalanceDTO.class)
                .returnResult().getResponseBody();

        GetBalanceDTO getSourceBalanceDTOAfterTwoTransactions = webTestClient.post().uri("/api/balances/transaction").bodyValue(transactionDTO)
                .headers(headers -> headers.setBasicAuth(username1, password)).exchange()
                .expectStatus().isOk()
                .expectBody(GetBalanceDTO.class)
                .returnResult().getResponseBody();

        GetBalanceDTO getSourceBalanceDTOAfterThreeTransactions = webTestClient.post().uri("/api/balances/transaction").bodyValue(transactionDTO)
                .headers(headers -> headers.setBasicAuth(username1, password)).exchange()
                .expectStatus().isOk()
                .expectBody(GetBalanceDTO.class)
                .returnResult().getResponseBody();

        ErrorResponse errorResponse = webTestClient.post().uri("/api/balances/transaction").bodyValue(transactionDTO)
                .headers(headers -> headers.setBasicAuth(username1, password)).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        //then
        assertNotEquals(BigDecimal.valueOf(90), getSourceBalanceDTOAfterOneTransaction.value());
        assertEquals(BigDecimal.valueOf(90).setScale(2, RoundingMode.HALF_UP), getSourceBalanceDTOAfterOneTransaction.value());
        assertEquals(1, getSourceBalanceDTOAfterOneTransaction.dailyTransactionsCount());

        assertNotEquals(BigDecimal.valueOf(80), getSourceBalanceDTOAfterTwoTransactions.value());
        assertEquals(BigDecimal.valueOf(80).setScale(2, RoundingMode.HALF_UP), getSourceBalanceDTOAfterTwoTransactions.value());
        assertEquals(2, getSourceBalanceDTOAfterTwoTransactions.dailyTransactionsCount());

        assertNotEquals(BigDecimal.valueOf(70), getSourceBalanceDTOAfterThreeTransactions.value());
        assertEquals(BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_UP), getSourceBalanceDTOAfterThreeTransactions.value());
        assertEquals(3, getSourceBalanceDTOAfterThreeTransactions.dailyTransactionsCount());

        assertEquals(1, errorResponse.reasons().size());
        assertEquals(transactionsLimitExceeded(userBalance1.id()), errorResponse.reasons().getFirst());
        assertNotNull(errorResponse.occurred());
        assertEquals(HttpStatus.BAD_REQUEST, errorResponse.status());

    }

    @Test
    void shouldThrowExceptionIfValueToSendIsInvalid() {
        //given
        BalanceCode code = BalanceCode.KOD_1;
        String username1 = "user111";
        String username2 = "user222";
        String password = "passworD1@";
        AddUserDTO addUserDTO1 = new AddUserDTO(username1, password, "111333555", "email1@wp.pl", NotificationChannel.SMS);
        AddUserDTO addUserDTO2 = new AddUserDTO(username2, password, "222444666", "email2@wp.pl", NotificationChannel.EMAIL);

        String expectedZeroValueErrorMsg = "Value to transact must be bigger than 0";
        String invalidValueFormatErrorMsg = "Value must contains max 10 digits before comma and max 2 after.";

        webTestClient.post().uri("/api/users/register").bodyValue(addUserDTO1)
                .exchange()
                .expectStatus().isCreated();

        webTestClient.post().uri("/api/users/register").bodyValue(addUserDTO2)
                .exchange()
                .expectStatus().isCreated();

        GetBalanceDTO userBalance1 = webTestClient.post().uri(uriBuilder -> uriBuilder.path("/api/balances")
                        .queryParam("code", code).build())
                .headers(headers -> headers.setBasicAuth(username1, password)).exchange()
                .expectStatus().isCreated()
                .expectBody(GetBalanceDTO.class)
                .returnResult().getResponseBody();

        GetBalanceDTO userBalance2 = webTestClient.post().uri(uriBuilder -> uriBuilder.path("/api/balances")
                        .queryParam("code", code).build())
                .headers(headers -> headers.setBasicAuth(username2, password)).exchange()
                .expectStatus().isCreated()
                .expectBody(GetBalanceDTO.class)
                .returnResult().getResponseBody();

        BalanceTransactionDTO transactionDTO1 = new BalanceTransactionDTO(userBalance1.id(), userBalance2.id(), BigDecimal.valueOf(0.0));
        BalanceTransactionDTO transactionDTO2 = new BalanceTransactionDTO(userBalance1.id(), userBalance2.id(), BigDecimal.valueOf(1.001));
        BalanceTransactionDTO transactionDTO3 = new BalanceTransactionDTO(userBalance1.id(), userBalance2.id(), BigDecimal.valueOf(10000000000L));

        //when
        ErrorResponse zeroValueError = webTestClient.post().uri("/api/balances/transaction").bodyValue(transactionDTO1)
                .headers(headers -> headers.setBasicAuth(username1, password)).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        ErrorResponse moreThanTwoDigitsAfterCommaError = webTestClient.post().uri("/api/balances/transaction").bodyValue(transactionDTO2)
                .headers(headers -> headers.setBasicAuth(username1, password)).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        ErrorResponse moreThanTenDigitsBeforeCommaError = webTestClient.post().uri("/api/balances/transaction").bodyValue(transactionDTO3)
                .headers(headers -> headers.setBasicAuth(username1, password)).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        //then
        assertEquals(1L, zeroValueError.reasons().size());
        assertEquals(expectedZeroValueErrorMsg, zeroValueError.reasons().getFirst());
        assertEquals(HttpStatus.BAD_REQUEST, zeroValueError.status());
        assertNotNull(zeroValueError.occurred());

        assertEquals(1L, moreThanTwoDigitsAfterCommaError.reasons().size());
        assertEquals(invalidValueFormatErrorMsg, moreThanTwoDigitsAfterCommaError.reasons().getFirst());
        assertEquals(HttpStatus.BAD_REQUEST, moreThanTwoDigitsAfterCommaError.status());
        assertNotNull(moreThanTwoDigitsAfterCommaError.occurred());

        assertEquals(1L, moreThanTenDigitsBeforeCommaError.reasons().size());
        assertEquals(invalidValueFormatErrorMsg, moreThanTenDigitsBeforeCommaError.reasons().getFirst());
        assertEquals(HttpStatus.BAD_REQUEST, moreThanTenDigitsBeforeCommaError.status());
        assertNotNull(moreThanTenDigitsBeforeCommaError.occurred());

    }
}