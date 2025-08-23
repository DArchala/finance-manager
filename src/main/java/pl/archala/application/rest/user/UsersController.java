package pl.archala.application.rest.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.archala.application.command.user.create.CreateUserApplicationService;
import pl.archala.application.command.user.create.CreateUserCommand;
import pl.archala.application.command.user.create.CreateUserResult;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UsersController {

    private final CreateUserApplicationService createUserApplicationService;

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/register")
    public CreateUserResult create(@RequestBody @Valid RestCreateUserRequest request) {
        var command = new CreateUserCommand(request.username(),
                                            request.password(),
                                            request.phone(),
                                            request.email(),
                                            request.notificationChannel());
        var createUserResult = createUserApplicationService.createUser(command);
        log.info("User {} has been registered", command.username());
        return createUserResult;
    }

}
