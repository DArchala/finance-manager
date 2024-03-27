package pl.archala.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.archala.PostgresqlContainer;
import pl.archala.dto.user.AddUserDTO;
import pl.archala.dto.user.GetUserDTO;
import pl.archala.enums.NotificationChannel;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsersControllerTest extends PostgresqlContainer {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldReturnAddedUserDto() {
        //given
        String username = "user123";
        String password = "password";
        String phone = "123456789";
        String email = "email@wp.pl";
        NotificationChannel channel = NotificationChannel.SMS;
        AddUserDTO addUserDTO = new AddUserDTO(username, password, phone, email, channel);

        //when
        GetUserDTO getUserDTO = webTestClient.post().uri("/api/users").bodyValue(addUserDTO).exchange()
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
}