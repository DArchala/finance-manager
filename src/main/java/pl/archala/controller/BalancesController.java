package pl.archala.controller;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.archala.dto.balance.GetBalanceDTO;
import pl.archala.enums.BalanceCode;
import pl.archala.exception.InsufficientFundsException;
import pl.archala.exception.TransactionsLimitException;
import pl.archala.exception.UserAlreadyContainsBalance;
import pl.archala.exception.UsersConflictException;
import pl.archala.service.balances.BalancesService;

import java.math.BigDecimal;
import java.security.Principal;

@Validated
@RestController
@RequestMapping("/api/balances")
@RequiredArgsConstructor
public class BalancesController {

    private final BalancesService balancesService;

    @PostMapping
    public ResponseEntity<GetBalanceDTO> create(@RequestParam BalanceCode balanceCode, Principal principal) throws UserAlreadyContainsBalance {
        return ResponseEntity.status(201).body(balancesService.create(balanceCode, principal.getName()));
    }

    @PostMapping("/transaction")
    public GetBalanceDTO makeTransaction(@RequestParam Long fromBalanceId,
                                         @RequestParam Long toBalanceId,
                                         @RequestParam @DecimalMin(value = "0.0", inclusive = false)
                                         @Digits(integer = 3, fraction = 2) BigDecimal value, Principal principal) throws InsufficientFundsException, TransactionsLimitException, UsersConflictException {
        return balancesService.makeTransaction(fromBalanceId, toBalanceId, value);
    }
}
