package pl.archala.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.archala.PostgresqlContainer;
import pl.archala.application.rest.error.ErrorResponse;
import pl.archala.application.command.user.create.CreateUserCommand;
import pl.archala.application.command.user.create.CreateUserResult;
import pl.archala.domain.notification.NotificationChannel;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UsersControllerTest extends PostgresqlContainer {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldReturnAddedUserDto() {
        //given
        String username = "user123";
        String password = "passworD1@";
        String phone = "123456789";
        String email = "email@wp.pl";
        NotificationChannel channel = NotificationChannel.SMS;
        CreateUserCommand createUserCommand = new CreateUserCommand(username, password, phone, email, channel);

        //when
        CreateUserResult createUserResult = webTestClient.post().uri("/api/users/register").bodyValue(createUserCommand).exchange()
                                             .expectStatus().isCreated()
                                             .expectBody(CreateUserResult.class)
                                             .returnResult().getResponseBody();

        //then
        assertNotNull(createUserResult.userId());
    }

    @Test
    void shouldReturnExceptionIfUsernameIsInvalid() {
        //given
        String password = "passworD1@";
        String phone = "123456789";
        String email = "email@wp.pl";
        NotificationChannel channel = NotificationChannel.SMS;

        CreateUserCommand createUserCommand1 = new CreateUserCommand("", password, phone, email, channel);
        CreateUserCommand createUserCommand2 = new CreateUserCommand("toooooooolooooooooongUsernameee", password, phone, email, channel);
        CreateUserCommand createUserCommand3 = new CreateUserCommand("short", password, phone, email, channel);

        String invalidUsernameMsg = "Username must contains min 6 and max 30 chars";
        String blankUsernameMsg = "Username must not be blank";

        //when
        //then
        webTestClient.post().uri("/api/users/register").bodyValue(createUserCommand1).exchange()
                     .expectStatus().isBadRequest()
                     .expectBody(ErrorResponse.class)
                     .value(err -> err.status().equals(HttpStatus.BAD_REQUEST))
                     .value(err -> Objects.equals(err.reasons().size(), 2))
                     .value(err -> err.reasons().containsAll(List.of(invalidUsernameMsg, blankUsernameMsg)));

        webTestClient.post().uri("/api/users/register").bodyValue(createUserCommand2).exchange()
                     .expectStatus().isBadRequest()
                     .expectBody(ErrorResponse.class)
                     .value(err -> Objects.equals(err.reasons().size(), 1))
                     .value(err -> err.reasons().contains(invalidUsernameMsg));

        webTestClient.post().uri("/api/users/register").bodyValue(createUserCommand3).exchange()
                     .expectStatus().isBadRequest()
                     .expectBody(ErrorResponse.class)
                     .value(err -> Objects.equals(err.reasons().size(), 1))
                     .value(err -> err.reasons().contains(invalidUsernameMsg));
    }

    @Test
    void shouldReturnErrorIfUserPasswordIsInvalid() {
        //given
        String username = "username123";
        String phone = "123456789";
        String email = "email@wp.pl";
        NotificationChannel channel = NotificationChannel.SMS;

        String invalidPasswordMsg = "Password should contains min 8 and max 30 chars including capital letter, digit and special char (@#$%^&+=)";
        String blankPasswordMsg = "User password must not be blank";

        CreateUserCommand blankPassUser = new CreateUserCommand(username, "", phone, email, channel);
        CreateUserCommand longPassUser = new CreateUserCommand(username, "tooLongPassword1@aaaaaaaaaaaaaa", phone, email, channel);
        CreateUserCommand noSpecialUser = new CreateUserCommand(username, "passWORD111", phone, email, channel);
        CreateUserCommand noDigitUser = new CreateUserCommand(username, "passWORD###", phone, email, channel);
        CreateUserCommand notAllowedCharsInPassUser = new CreateUserCommand(username, "passWORD@;;;...__ .", phone, email, channel);

        //when
        //then
        webTestClient.post().uri("/api/users/register").bodyValue(blankPassUser).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(err -> err.status().equals(HttpStatus.BAD_REQUEST))
                .value(err -> Objects.equals(err.reasons().size(), 2))
                .value(err -> err.reasons().containsAll(List.of(invalidPasswordMsg, blankPasswordMsg)));

        webTestClient.post().uri("/api/users/register").bodyValue(longPassUser).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(err -> err.status().equals(HttpStatus.BAD_REQUEST))
                .value(err -> Objects.equals(err.reasons().size(), 1))
                .value(err -> err.reasons().contains(invalidPasswordMsg));

        webTestClient.post().uri("/api/users/register").bodyValue(noSpecialUser).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(err -> err.status().equals(HttpStatus.BAD_REQUEST))
                .value(err -> Objects.equals(err.reasons().size(), 1))
                .value(err -> err.reasons().contains(invalidPasswordMsg));

        webTestClient.post().uri("/api/users/register").bodyValue(noDigitUser).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(err -> err.status().equals(HttpStatus.BAD_REQUEST))
                .value(err -> Objects.equals(err.reasons().size(), 1))
                .value(err -> err.reasons().contains(invalidPasswordMsg));

        webTestClient.post().uri("/api/users/register").bodyValue(notAllowedCharsInPassUser).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(err -> err.status().equals(HttpStatus.BAD_REQUEST))
                .value(err -> Objects.equals(err.reasons().size(), 1))
                .value(err -> err.reasons().contains(invalidPasswordMsg));
    }

    @Test
    void shouldReturnErrorIfPhoneIsInvalid() {
        //given
        String username = "username123";
        String password = "passWORD1@";
        String email = "email@wp.pl";
        NotificationChannel channel = NotificationChannel.SMS;

        String invalidPhoneMsg = "User phone number must contains exactly 9 digits";
        String blankPhoneMsg = "User phone number must not be blank";

        CreateUserCommand blankPhoneUser = new CreateUserCommand(username, password, "", email, channel);
        CreateUserCommand invalidPhone1User = new CreateUserCommand(username, password, "AAABBBDDD", email, channel);
        CreateUserCommand invalidPhone2User = new CreateUserCommand(username, password, "@@@555ZZZ", email, channel);

        //when
        //then
        webTestClient.post().uri("/api/users/register").bodyValue(blankPhoneUser).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(err -> err.status().equals(HttpStatus.BAD_REQUEST))
                .value(err -> Objects.equals(err.reasons().size(), 2))
                .value(err -> err.reasons().containsAll(List.of(blankPhoneMsg, invalidPhoneMsg)));

        webTestClient.post().uri("/api/users/register").bodyValue(invalidPhone1User).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(err -> err.status().equals(HttpStatus.BAD_REQUEST))
                .value(err -> Objects.equals(err.reasons().size(), 1))
                .value(err -> err.reasons().contains(invalidPhoneMsg));

        webTestClient.post().uri("/api/users/register").bodyValue(invalidPhone2User).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(err -> err.status().equals(HttpStatus.BAD_REQUEST))
                .value(err -> Objects.equals(err.reasons().size(), 1))
                .value(err -> err.reasons().contains(invalidPhoneMsg));
    }

    @Test
    void shouldReturnErrorIfEmailIsInvalid() {
        //given
        String username = "username123";
        String phone = "123456789";
        String password = "passWORD1@";
        NotificationChannel channel = NotificationChannel.SMS;

        String blankEmailMsg = "User e-mail must not be blank";
        String invalidEmailMsg = "User e-mail must be a well-formed email address";

        CreateUserCommand blankEmailUser = new CreateUserCommand(username, password, phone, "", channel);
        CreateUserCommand invalidEmailUser1 = new CreateUserCommand(username, password, phone, "invalidemail", channel);
        CreateUserCommand invalidEmailUser2 = new CreateUserCommand(username, password, phone, "invalidemail@.", channel);
        CreateUserCommand invalidEmailUser3 = new CreateUserCommand(username, password, phone, "invalidemail@com", channel);
        CreateUserCommand invalidEmailUser4 = new CreateUserCommand(username, password, phone, "invalidemail.com", channel);
        CreateUserCommand invalidEmailUser5 = new CreateUserCommand(username, password, phone, "@gmail.com", channel);

        //when
        //then
        webTestClient.post().uri("/api/users/register").bodyValue(blankEmailUser).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(err -> err.status().equals(HttpStatus.BAD_REQUEST))
                .value(err -> Objects.equals(err.reasons().size(), 1))
                .value(err -> err.reasons().contains(blankEmailMsg))
                .returnResult().getResponseBody();

        webTestClient.post().uri("/api/users/register").bodyValue(invalidEmailUser1).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(err -> err.status().equals(HttpStatus.BAD_REQUEST))
                .value(err -> Objects.equals(err.reasons().size(), 1))
                .value(err -> err.reasons().contains(invalidEmailMsg))
                .returnResult().getResponseBody();

        webTestClient.post().uri("/api/users/register").bodyValue(invalidEmailUser2).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(err -> err.status().equals(HttpStatus.BAD_REQUEST))
                .value(err -> Objects.equals(err.reasons().size(), 1))
                .value(err -> err.reasons().contains(invalidEmailMsg))
                .returnResult().getResponseBody();

        webTestClient.post().uri("/api/users/register").bodyValue(invalidEmailUser3).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(err -> err.status().equals(HttpStatus.BAD_REQUEST))
                .value(err -> Objects.equals(err.reasons().size(), 1))
                .value(err -> err.reasons().contains(invalidEmailMsg))
                .returnResult().getResponseBody();

        webTestClient.post().uri("/api/users/register").bodyValue(invalidEmailUser4).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(err -> err.status().equals(HttpStatus.BAD_REQUEST))
                .value(err -> Objects.equals(err.reasons().size(), 1))
                .value(err -> err.reasons().contains(invalidEmailMsg))
                .returnResult().getResponseBody();

        webTestClient.post().uri("/api/users/register").bodyValue(invalidEmailUser5).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(err -> err.status().equals(HttpStatus.BAD_REQUEST))
                .value(err -> Objects.equals(err.reasons().size(), 1))
                .value(err -> err.reasons().contains(invalidEmailMsg))
                .returnResult().getResponseBody();
    }

    @Test
    void shouldNotAllowToAddTwoUsersWithEqualUsernames() {
        //given
        String username = "user123";
        NotificationChannel channel = NotificationChannel.SMS;
        CreateUserCommand createUserCommand1 = new CreateUserCommand(username, "passworD1@", "123456789", "email321@gmail.com", channel);
        CreateUserCommand createUserCommand2 = new CreateUserCommand(username, "passworD1@", "987654321", "email123@wp.pl", channel);

        String expectedUsernameTakenMsg = "Provided username %s is already taken".formatted(username);

        //when
        //then
        webTestClient.post().uri("/api/users/register").bodyValue(createUserCommand1).exchange()
                     .expectStatus().isCreated()
                     .expectBody(CreateUserResult.class)
                     .returnResult().getResponseBody();

        webTestClient.post().uri("/api/users/register").bodyValue(createUserCommand2).exchange()
                     .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                     .expectBody(ErrorResponse.class)
                     .value(err -> Objects.equals(err.reasons().size(), 1))
                     .value(err -> err.reasons().contains(expectedUsernameTakenMsg));

    }

    @Test
    void shouldNotAllowToAddTwoUsersWithEqualEmails() {
        //given
        String password = "passworD1@";
        String email = "email@wp.pl";
        NotificationChannel channel = NotificationChannel.SMS;
        CreateUserCommand createUserCommand1 = new CreateUserCommand("user123", password, "123456789", email, channel);
        CreateUserCommand createUserCommand2 = new CreateUserCommand("user321", password, "987654321", email, channel);

        String expectedEmailTakenMsg = "Provided email %s is already taken".formatted(email);

        //when
        //then
        webTestClient.post().uri("/api/users/register").bodyValue(createUserCommand1).exchange()
                     .expectStatus().isCreated()
                     .expectBody(CreateUserResult.class)
                     .returnResult().getResponseBody();

        webTestClient.post().uri("/api/users/register").bodyValue(createUserCommand2).exchange()
                     .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                     .expectBody(ErrorResponse.class)
                     .value(err -> Objects.equals(err.reasons().size(), 1))
                     .value(err -> err.reasons().contains(expectedEmailTakenMsg));
    }

    @Test
    void shouldNotAllowToAddTwoUsersWithEqualPhones() {
        //given
        String password = "passworD1@";
        String phone = "123456789";
        NotificationChannel channel = NotificationChannel.SMS;
        CreateUserCommand createUserCommand1 = new CreateUserCommand("user123", password, phone, "email123@wp.pl", channel);
        CreateUserCommand createUserCommand2 = new CreateUserCommand("user321", password, phone, "email321@gmail.com", channel);

        String expectedPhoneTakenMsg = "Provided phone %s is already taken".formatted(phone);

        //when
        //then
        webTestClient.post().uri("/api/users/register").bodyValue(createUserCommand1).exchange()
                     .expectStatus().isCreated()
                     .expectBody(CreateUserResult.class)
                     .returnResult().getResponseBody();

        webTestClient.post().uri("/api/users/register").bodyValue(createUserCommand2).exchange()
                     .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                     .expectBody(ErrorResponse.class)
                     .value(err -> Objects.equals(err.reasons().size(), 1))
                     .value(err -> err.reasons().contains(expectedPhoneTakenMsg));
    }
}