package pl.archala.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.archala.components.NotificationFactory;
import pl.archala.dto.balance.BalanceTransactionDTO;
import pl.archala.dto.balance.GetBalanceDTO;
import pl.archala.enums.BalanceCode;
import pl.archala.exception.*;
import pl.archala.service.balances.BalancesService;
import pl.archala.service.users.UsersService;

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
    public GetBalanceDTO makeTransaction(@Valid @RequestBody BalanceTransactionDTO transactionDTO,
                                         Principal principal)
            throws InsufficientFundsException, TransactionsLimitException, UserException, UnsupportedNotificationTypeException {
        var getBalanceDTO = balancesService.makeTransaction(transactionDTO, principal.getName());
        var userNotificationData = usersService.getUserNotificationData(principal.getName());
        notificationFactory.execute(userNotificationData, transactionDTO.value(), transactionDTO.targetBalanceId(), getBalanceDTO.value());
        return getBalanceDTO;
    }
}
