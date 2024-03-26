package pl.archala.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.archala.dto.AddUserDTO;
import pl.archala.dto.GetUserDTO;
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
