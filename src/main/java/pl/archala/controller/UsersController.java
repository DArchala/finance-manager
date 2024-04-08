package pl.archala.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.archala.dto.user.AddUserDTO;
import pl.archala.dto.user.GetUserDTO;
import pl.archala.exception.UsersConflictException;
import pl.archala.service.users.UsersService;

import java.util.Objects;

import static pl.archala.utils.EndpointProvider.API_USERS;
import static pl.archala.utils.EndpointProvider.REGISTER;

@Validated
@RestController
@RequestMapping(API_USERS)
@RequiredArgsConstructor
@Slf4j
public class UsersController {

    private final UsersService usersService;

    @PostMapping(REGISTER)
    public ResponseEntity<GetUserDTO> register(@Valid @RequestBody AddUserDTO addUserDTO) throws UsersConflictException {
        GetUserDTO getUserDTO = usersService.registerUser(addUserDTO);
        log.info("User {} has been registered", Objects.requireNonNull(getUserDTO).username());
        return ResponseEntity.status(201).body(getUserDTO);
    }

}
