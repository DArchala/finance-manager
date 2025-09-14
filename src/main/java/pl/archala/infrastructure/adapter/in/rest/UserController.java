package pl.archala.infrastructure.adapter.in.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.archala.application.api.user.RestCreateUserRequest;
import pl.archala.application.command.user.create.CreateUserApplicationService;
import pl.archala.application.command.user.create.CreateUserCommand;
import pl.archala.application.query.find_user_balance_details.FindUserBalanceDetails;
import pl.archala.application.query.find_user_balance_details.FindUserBalanceDetailsQuery;
import pl.archala.application.query.find_user_balance_details.FindUserBalanceDetailsView;
import pl.archala.application.query.find_user_details.FindUserDetails;
import pl.archala.application.query.find_user_details.FindUserDetailsQuery;
import pl.archala.application.query.find_user_details.FindUserDetailsView;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final CreateUserApplicationService createUserApplicationService;
    private final FindUserBalanceDetails findUserBalanceDetails;
    private final FindUserDetails findUserDetails;

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/create")
    public void create(@RequestBody @Valid RestCreateUserRequest request) {
        log.info("Create user request incoming: {}", request);

        var command = new CreateUserCommand(request.name(),
                                            request.password(),
                                            request.phone(),
                                            request.email(),
                                            request.notificationChannel());
        createUserApplicationService.createUser(command);

        log.info("User with name: {} has been created", request.name());
    }

    @GetMapping("/balance-details")
    public FindUserBalanceDetailsView getUserWithBalanceDetails(Principal principal) {
        var filter = new FindUserBalanceDetailsQuery(principal.getName());
        return findUserBalanceDetails.findUserBalanceDetails(filter);
    }


    @GetMapping("/user-details")
    public FindUserDetailsView getUserDetails(Principal principal) {
        var filter = new FindUserDetailsQuery(principal.getName());
        return findUserDetails.findUserDetails(filter);
    }

}
