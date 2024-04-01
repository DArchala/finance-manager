package pl.archala.controller;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.archala.components.NotificationFactory;
import pl.archala.dto.balance.GetBalanceDTO;
import pl.archala.enums.BalanceCode;
import pl.archala.exception.InsufficientFundsException;
import pl.archala.exception.TransactionsLimitException;
import pl.archala.exception.UserAlreadyContainsBalanceException;
import pl.archala.exception.UserException;
import pl.archala.service.balances.BalancesService;
import pl.archala.service.users.UsersService;

import java.math.BigDecimal;
import java.security.Principal;

@Validated
@RestController
@RequestMapping("/api/balances")
@RequiredArgsConstructor
@Slf4j
public class BalancesController {

    private final BalancesService balancesService;
    private final UsersService usersService;
    private final NotificationFactory notificationFactory;

    @PostMapping
    public ResponseEntity<GetBalanceDTO> create(@RequestParam BalanceCode code, Principal principal)
            throws UserAlreadyContainsBalanceException {
        GetBalanceDTO getBalanceDTO = balancesService.create(code, principal.getName());
        log.info("Balance with id {} has been created with value {} for user {}", getBalanceDTO.id(), code.getValue(), principal.getName());
        return ResponseEntity.status(201).body(getBalanceDTO);
    }

    @PostMapping("/transaction")
    public GetBalanceDTO makeTransaction(@RequestParam String sourceBalanceId,
                                         @RequestParam String targetBalanceId,
                                         @RequestParam @DecimalMin(value = "0.0", inclusive = false,
                                                 message = "Value to transact must be bigger than 0")
                                         @Digits(integer = 10, fraction = 2, message = "Value must contains max 10 digits before comma and max 2 after.") BigDecimal value,
                                         Principal principal)
            throws InsufficientFundsException, TransactionsLimitException, UserException {
        var getBalanceDTO = balancesService.makeTransaction(sourceBalanceId, targetBalanceId, value, principal.getName());
        var userNotificationData = usersService.getUserNotificationData(principal.getName());
        notificationFactory.execute(userNotificationData, value, targetBalanceId, getBalanceDTO.value());
        return getBalanceDTO;
    }
}
