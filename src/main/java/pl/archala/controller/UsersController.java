package pl.archala.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.archala.dto.user.AddUserDTO;
import pl.archala.dto.user.GetUserDTO;
import pl.archala.service.UsersService;

@Validated
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService service;

    @PostMapping
    public ResponseEntity<GetUserDTO> save(@Valid AddUserDTO addUserDTO) {
        return ResponseEntity.status(201).body(service.registerUser(addUserDTO));
    }

}
