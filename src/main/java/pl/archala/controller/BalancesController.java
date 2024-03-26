package pl.archala.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.archala.dto.balance.GetBalanceDTO;
import pl.archala.exception.UserAlreadyContainsBalance;
import pl.archala.service.BalancesService;

import java.security.Principal;

@Validated
@RestController
@RequestMapping("/api/balances")
@RequiredArgsConstructor
public class BalancesController {

    private final BalancesService balancesService;

    @GetMapping("/details/{id}")
    public GetBalanceDTO findById(@PathVariable Long id) {
        return balancesService.findById(id);
    }

    @PostMapping
    public ResponseEntity<GetBalanceDTO> create(Principal principal) throws UserAlreadyContainsBalance {
        return ResponseEntity.status(201).body(balancesService.save(principal.getName()));
    }

}
