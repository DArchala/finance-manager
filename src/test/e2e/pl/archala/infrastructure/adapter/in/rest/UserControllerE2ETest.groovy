package pl.archala.infrastructure.adapter.in.rest


import pl.archala.BaseE2ESpecification
import pl.archala.application.api.error.ErrorCode
import pl.archala.application.api.error.ErrorResponse
import pl.archala.application.api.user.RestCreateUserRequest
import pl.archala.domain.notification.NotificationChannel
import pl.archala.domain.user.User

class UserControllerE2ETest extends BaseE2ESpecification {

    def 'Should create user'() {
        given: 'Prepare create user request'
        def request = new RestCreateUserRequest("username",
                                                "Password@1",
                                                "700500300",
                                                "username@email.com",
                                                NotificationChannel.EMAIL)

        when: 'Create user by api'
        webTestClient.post().uri("/api/users/create")
                     .bodyValue(request)
                     .exchange()
                     .expectStatus().isCreated()

        then:
        def users = userRepository.findAll()
        assert users.size() == 1

        verifyAll(users.first()) {
            it.getId() != null
            it.getName() == request.name()
            it.getPassword() != null
            it.getPhone() == request.phone()
            it.getEmail() == request.email()
            it.getNotificationChannel() == request.notificationChannel()
            it.getBalance() == null
            it.getVersion() != null
        }

    }

    def 'Should throw exception if user name, phone or email is already taken'() {
        given: 'Prepare existing user'
        def user = User.create("username",
                               userPasswordEncoder.encode("Password1@"),
                               "111222333",
                               "username@email.com",
                               NotificationChannel.EMAIL)
        userRepository.persist(user)

        and: 'Prepare create user request'
        def request = new RestCreateUserRequest(username, "Password1@", phone, email, NotificationChannel.EMAIL)

        when: 'Create user by api'
        def errorResponse = webTestClient.post().uri("/api/users/create")
                                         .bodyValue(request)
                                         .exchange()
                                         .expectBody(ErrorResponse.class)
                                         .returnResult().getResponseBody()

        then:
        errorResponse.reasons().size() == 1
        errorResponse.reasons().first() == expectedResponseMessage
        errorResponse.errorCode() == ErrorCode.BAD_REQUEST
        errorResponse.occurred() != null

        where:
        username       | phone       | email                 | expectedResponseMessage
        "username"     | "900800700" | "different@email.com" | "User name: username is already taken"
        "diffUsername" | "111222333" | "different@email.com" | "User phone: 111222333 is already taken"
        "diffUsername" | "900800700" | "username@email.com"  | "User email: username@email.com is already taken"

    }

    def cleanup() {
        userRepository.deleteAll()
    }

}
