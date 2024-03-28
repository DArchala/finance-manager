package pl.archala.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.archala.PostgresqlContainer;
import pl.archala.dto.errorResponse.ErrorResponse;
import pl.archala.dto.user.AddUserDTO;
import pl.archala.dto.user.GetUserDTO;
import pl.archala.enums.NotificationChannel;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.archala.utils.StringInfoProvider.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UsersControllerTest extends PostgresqlContainer {

    @Autowired
    private WebTestClient rest;

    @Test
    void shouldReturnAddedUserDto() {
        //given
        String username = "user123";
        String password = "passworD1@";
        String phone = "123456789";
        String email = "email@wp.pl";
        NotificationChannel channel = NotificationChannel.SMS;
        AddUserDTO addUserDTO = new AddUserDTO(username, password, phone, email, channel);

        //when
        GetUserDTO getUserDTO = rest.post().uri("/api/users").bodyValue(addUserDTO).exchange()
                .expectStatus().isCreated()
                .expectBody(GetUserDTO.class)
                .returnResult().getResponseBody();

        //then
        assertEquals(1L, getUserDTO.id());
        assertEquals(username, getUserDTO.username());
        assertEquals(phone, getUserDTO.phone());
        assertEquals(email, getUserDTO.email());
        assertEquals(NotificationChannel.SMS, getUserDTO.notificationChannel());

    }

    @Test
    void shouldReturnExceptionIfUsernameIsInvalid() {
        //given
        String password = "passworD1@";
        String phone = "123456789";
        String email = "email@wp.pl";
        NotificationChannel channel = NotificationChannel.SMS;

        AddUserDTO addUserDTO1 = new AddUserDTO("", password, phone, email, channel);
        AddUserDTO addUserDTO2 = new AddUserDTO("toooooooolooooooooongUsernameee", password, phone, email, channel);
        AddUserDTO addUserDTO3 = new AddUserDTO("short", password, phone, email, channel);

        String invalidUsernameMsg = "Username must contains min 6 and max 30 chars";
        String blankUsernameMsg = "Username must not be blank";

        //when
        //then
        rest.post().uri("/api/users").bodyValue(addUserDTO1).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(err -> err.status().equals(HttpStatus.BAD_REQUEST))
                .value(err -> Objects.equals(err.reasons().size(), 2))
                .value(err -> err.reasons().containsAll(List.of(invalidUsernameMsg, blankUsernameMsg)));

        rest.post().uri("/api/users").bodyValue(addUserDTO2).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(err -> Objects.equals(err.reasons().size(), 1))
                .value(err -> err.reasons().contains(invalidUsernameMsg));

        rest.post().uri("/api/users").bodyValue(addUserDTO3).exchange()
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

        AddUserDTO blankPassUser = new AddUserDTO(username, "", phone, email, channel);
        AddUserDTO longPassUser = new AddUserDTO(username, "tooLongPassword1@aaaaaaaaaaaaaa", phone, email, channel);
        AddUserDTO noSpecialUser = new AddUserDTO(username, "passWORD111", phone, email, channel);
        AddUserDTO noDigitUser = new AddUserDTO(username, "passWORD###", phone, email, channel);
        AddUserDTO notAllowedCharsInPassUser = new AddUserDTO(username, "passWORD@;;;...__ .", phone, email, channel);

        //when
        //then
        rest.post().uri("/api/users").bodyValue(blankPassUser).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(err -> err.status().equals(HttpStatus.BAD_REQUEST))
                .value(err -> Objects.equals(err.reasons().size(), 2))
                .value(err -> err.reasons().containsAll(List.of(invalidPasswordMsg, blankPasswordMsg)));

        rest.post().uri("/api/users").bodyValue(longPassUser).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(err -> err.status().equals(HttpStatus.BAD_REQUEST))
                .value(err -> Objects.equals(err.reasons().size(), 1))
                .value(err -> err.reasons().contains(invalidPasswordMsg));

        rest.post().uri("/api/users").bodyValue(noSpecialUser).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(err -> err.status().equals(HttpStatus.BAD_REQUEST))
                .value(err -> Objects.equals(err.reasons().size(), 1))
                .value(err -> err.reasons().contains(invalidPasswordMsg));

        rest.post().uri("/api/users").bodyValue(noDigitUser).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(err -> err.status().equals(HttpStatus.BAD_REQUEST))
                .value(err -> Objects.equals(err.reasons().size(), 1))
                .value(err -> err.reasons().contains(invalidPasswordMsg));

        rest.post().uri("/api/users").bodyValue(notAllowedCharsInPassUser).exchange()
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

        AddUserDTO blankPhoneUser = new AddUserDTO(username, password, "", email, channel);
        AddUserDTO invalidPhone1User = new AddUserDTO(username, password, "AAABBBDDD", email, channel);
        AddUserDTO invalidPhone2User = new AddUserDTO(username, password, "@@@555ZZZ", email, channel);

        //when
        //then
        rest.post().uri("/api/users").bodyValue(blankPhoneUser).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(err -> err.status().equals(HttpStatus.BAD_REQUEST))
                .value(err -> Objects.equals(err.reasons().size(), 2))
                .value(err -> err.reasons().containsAll(List.of(blankPhoneMsg, invalidPhoneMsg)));

        rest.post().uri("/api/users").bodyValue(invalidPhone1User).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(err -> err.status().equals(HttpStatus.BAD_REQUEST))
                .value(err -> Objects.equals(err.reasons().size(), 1))
                .value(err -> err.reasons().contains(invalidPhoneMsg));

        rest.post().uri("/api/users").bodyValue(invalidPhone2User).exchange()
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

        AddUserDTO blankEmailUser = new AddUserDTO(username, password, phone, "", channel);
        AddUserDTO invalidEmailUser1 = new AddUserDTO(username, password, phone, "invalidemail", channel);
        AddUserDTO invalidEmailUser2 = new AddUserDTO(username, password, phone, "invalidemail@.", channel);
        AddUserDTO invalidEmailUser3 = new AddUserDTO(username, password, phone, "invalidemail@com", channel);
        AddUserDTO invalidEmailUser4 = new AddUserDTO(username, password, phone, "invalidemail.com", channel);
        AddUserDTO invalidEmailUser5 = new AddUserDTO(username, password, phone, "@gmail.com", channel);

        //when
        //then
        rest.post().uri("/api/users").bodyValue(blankEmailUser).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(err -> err.status().equals(HttpStatus.BAD_REQUEST))
                .value(err -> Objects.equals(err.reasons().size(), 1))
                .value(err -> err.reasons().contains(blankEmailMsg))
                .returnResult().getResponseBody();

        rest.post().uri("/api/users").bodyValue(invalidEmailUser1).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(err -> err.status().equals(HttpStatus.BAD_REQUEST))
                .value(err -> Objects.equals(err.reasons().size(), 1))
                .value(err -> err.reasons().contains(invalidEmailMsg))
                .returnResult().getResponseBody();

        rest.post().uri("/api/users").bodyValue(invalidEmailUser2).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(err -> err.status().equals(HttpStatus.BAD_REQUEST))
                .value(err -> Objects.equals(err.reasons().size(), 1))
                .value(err -> err.reasons().contains(invalidEmailMsg))
                .returnResult().getResponseBody();

        rest.post().uri("/api/users").bodyValue(invalidEmailUser3).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(err -> err.status().equals(HttpStatus.BAD_REQUEST))
                .value(err -> Objects.equals(err.reasons().size(), 1))
                .value(err -> err.reasons().contains(invalidEmailMsg))
                .returnResult().getResponseBody();

        rest.post().uri("/api/users").bodyValue(invalidEmailUser4).exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(err -> err.status().equals(HttpStatus.BAD_REQUEST))
                .value(err -> Objects.equals(err.reasons().size(), 1))
                .value(err -> err.reasons().contains(invalidEmailMsg))
                .returnResult().getResponseBody();

        rest.post().uri("/api/users").bodyValue(invalidEmailUser5).exchange()
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
        AddUserDTO addUserDTO1 = new AddUserDTO(username, "passworD1@", "123456789", "email321@gmail.com", channel);
        AddUserDTO addUserDTO2 = new AddUserDTO(username, "passworD1@", "987654321", "email123@wp.pl", channel);

        String expectedUsernameTakenMsg = USERNAME_IS_ALREADY_TAKEN.formatted(username);

        //when
        //then
        rest.post().uri("/api/users").bodyValue(addUserDTO1).exchange()
                .expectStatus().isCreated()
                .expectBody(GetUserDTO.class)
                .returnResult().getResponseBody();

        rest.post().uri("/api/users").bodyValue(addUserDTO2).exchange()
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
        AddUserDTO addUserDTO1 = new AddUserDTO("user123", password, "123456789", email, channel);
        AddUserDTO addUserDTO2 = new AddUserDTO("user321", password, "987654321", email, channel);

        String expectedEmailTakenMsg = EMAIL_IS_ALREADY_TAKEN.formatted(email);

        //when
        //then
        rest.post().uri("/api/users").bodyValue(addUserDTO1).exchange()
                .expectStatus().isCreated()
                .expectBody(GetUserDTO.class)
                .returnResult().getResponseBody();

        rest.post().uri("/api/users").bodyValue(addUserDTO2).exchange()
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
        AddUserDTO addUserDTO1 = new AddUserDTO("user123", password, phone, "email123@wp.pl", channel);
        AddUserDTO addUserDTO2 = new AddUserDTO("user321", password, phone, "email321@gmail.com", channel);

        String expectedPhoneTakenMsg = PHONE_IS_ALREADY_TAKEN.formatted(phone);

        //when
        //then
        rest.post().uri("/api/users").bodyValue(addUserDTO1).exchange()
                .expectStatus().isCreated()
                .expectBody(GetUserDTO.class)
                .returnResult().getResponseBody();

        rest.post().uri("/api/users").bodyValue(addUserDTO2).exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody(ErrorResponse.class)
                .value(err -> Objects.equals(err.reasons().size(), 1))
                .value(err -> err.reasons().contains(expectedPhoneTakenMsg));
    }
}