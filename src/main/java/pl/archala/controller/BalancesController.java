package pl.archala.controller;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.archala.dto.balance.GetBalanceDTO;
import pl.archala.enums.BalanceCode;
import pl.archala.exception.InsufficientFundsException;
import pl.archala.exception.UserAlreadyContainsBalance;
import pl.archala.service.BalancesService;

import java.math.BigDecimal;
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
    public ResponseEntity<GetBalanceDTO> create(@RequestParam BalanceCode balanceCode, Principal principal) throws UserAlreadyContainsBalance {
        return ResponseEntity.status(201).body(balancesService.create(balanceCode, principal.getName()));
    }

    @PostMapping("/transaction")
    public GetBalanceDTO makeTransaction(Long fromBalanceId, Long toBalanceId, @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer=3, fraction=2) BigDecimal value) throws InsufficientFundsException {
        return balancesService.makeTransaction(fromBalanceId, toBalanceId, value);
    }
}
