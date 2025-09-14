package pl.archala.infrastructure.adapter.in.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.archala.application.api.balance.RestCreateBalanceRequest;
import pl.archala.application.api.send_money.RestSendMoneyRequest;
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
        log.info("Create balance request incoming: {}, invoked by user: {}", request, principal.getName());

        var command = new CreateBalanceCommand(request.balanceCode(), principal.getName());
        var createBalanceResult = createBalanceApplicationService.createBalance(command);

        log.info("Balance with id: {} has been created for user: {}", createBalanceResult.balanceId(), principal.getName());
        return createBalanceResult;
    }

    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @PostMapping("/send-money")
    public void sendMoney(@RequestBody @Valid RestSendMoneyRequest request, Principal principal) {
        log.info("Send money request incoming: {}, invoked by user: {}", request, principal.getName());

        var command = new SendMoneyCommand(principal.getName(),
                                           request.targetBalanceId(),
                                           request.amount());
        sendMoneyApplicationService.sendMoney(command);
        log.info("Money with amount: {} has been sent from balance belong to user: {} to balance with id: {} ",
                 request.amount(),
                 principal.getName(),
                 request.targetBalanceId());
    }
}
