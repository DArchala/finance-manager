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

@Validated
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UsersController {

    private final UsersService service;

    @PostMapping("/register")
    public ResponseEntity<GetUserDTO> save(@Valid @RequestBody AddUserDTO addUserDTO) throws UsersConflictException {
        var getUserDTO = ResponseEntity.status(201).body(service.registerUser(addUserDTO));
        log.info("User {} has been registered", Objects.requireNonNull(getUserDTO.getBody()).username());
        return getUserDTO;
    }

}
