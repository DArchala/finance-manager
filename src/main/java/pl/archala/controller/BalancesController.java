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
import pl.archala.exception.*;
import pl.archala.service.balances.BalancesService;

import java.math.BigDecimal;

@Validated
@RestController
@RequestMapping("/api/balances")
@RequiredArgsConstructor
public class BalancesController {

    private final BalancesService balancesService;

    @PostMapping
    public ResponseEntity<GetBalanceDTO> create(@RequestParam BalanceCode balanceCode, String username)
            throws UserAlreadyContainsBalance {
        return ResponseEntity.status(201).body(balancesService.create(balanceCode, username));
    }

    @PostMapping("/transaction")
    public GetBalanceDTO makeTransaction(@RequestParam Long sourceBalanceId,
                                         @RequestParam Long targetBalanceId,
                                         @RequestParam @DecimalMin(value = "0.0", inclusive = false,
                                                 message = "Value to transact must be bigger than 0")
                                         @Digits(integer = Integer.MAX_VALUE, fraction = 2, message = "Value should has a maximum of 2 decimal digits") BigDecimal value,
                                         @RequestParam String username)
            throws InsufficientFundsException, TransactionsLimitException, UsersConflictException, UserException {
        return balancesService.makeTransaction(sourceBalanceId, targetBalanceId, value, username);
    }
}
