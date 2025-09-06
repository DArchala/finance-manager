package pl.archala.infrastructure.adapter.in.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.archala.application.api.balance.RestCreateBalanceRequest;
import pl.archala.application.api.balance.RestSendMoneyRequest;
import pl.archala.application.command.balance.create.CreateBalanceApplicationService;
import pl.archala.application.command.balance.create.CreateBalanceCommand;
import pl.archala.application.command.balance.create.CreateBalanceResult;
import pl.archala.application.command.balance.send_money.SendMoneyApplicationService;
import pl.archala.application.command.balance.send_money.SendMoneyCommand;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/balance")
public class BalanceController {

    private final CreateBalanceApplicationService createBalanceApplicationService;
    private final SendMoneyApplicationService sendMoneyApplicationService;

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping
    public CreateBalanceResult create(@RequestBody @Valid RestCreateBalanceRequest request, Principal principal) {
        var command = new CreateBalanceCommand(request.balanceCode(), principal.getName());
        var createUserBalanceResult = createBalanceApplicationService.createBalance(command);
        log.info("Balance with id {} has been created with amount {} for user {}",
                 createUserBalanceResult.balanceId(),
                 request.balanceCode()
                        .getValue(),
                 principal.getName());

        return createUserBalanceResult;
    }

    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @PostMapping("/send-money")
    public void sendMoney(@Valid @RequestBody RestSendMoneyRequest request, Principal principal) {
        var command = new SendMoneyCommand(principal.getName(),
                                           request.targetBalanceId(),
                                           request.amount());
        sendMoneyApplicationService.sendMoney(command);
        log.info("Money has been sent from balance belong to user: {} to balance with id: {} with amount: {}",
                 principal.getName(),
                 request.targetBalanceId(),
                 request.amount());
    }
}
