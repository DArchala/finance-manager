package pl.archala.controller;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.archala.components.NotificationFactory;
import pl.archala.dto.balance.GetBalanceDTO;
import pl.archala.dto.user.UserNotificationData;
import pl.archala.enums.BalanceCode;
import pl.archala.exception.*;
import pl.archala.service.balances.BalancesService;
import pl.archala.service.users.UsersService;

import java.math.BigDecimal;
import java.security.Principal;

@Validated
@RestController
@RequestMapping("/api/balances")
@RequiredArgsConstructor
public class BalancesController {

    private final BalancesService balancesService;
    private final UsersService usersService;
    private final NotificationFactory notificationFactory;

    @PostMapping
    public ResponseEntity<GetBalanceDTO> create(@RequestParam BalanceCode code, Principal principal)
            throws UserAlreadyContainsBalanceException {
        return ResponseEntity.status(201).body(balancesService.create(code, principal.getName()));
    }

    @PostMapping("/transaction")
    public GetBalanceDTO makeTransaction(@RequestParam String sourceBalanceId,
                                         @RequestParam String targetBalanceId,
                                         @RequestParam @DecimalMin(value = "0.0", inclusive = false,
                                                 message = "Value to transact must be bigger than 0")
                                         @Digits(integer = 10, fraction = 2, message = "Value should has a maximum of 2 decimal digits") BigDecimal value,
                                         Principal principal)
            throws InsufficientFundsException, TransactionsLimitException, UserException {
        GetBalanceDTO getBalanceDTO = balancesService.makeTransaction(sourceBalanceId, targetBalanceId, value, principal.getName());
        UserNotificationData userNotificationData = usersService.getUserNotificationData(principal.getName());
        notificationFactory.execute(userNotificationData, value, targetBalanceId, getBalanceDTO.value());
        return getBalanceDTO;
    }
}
