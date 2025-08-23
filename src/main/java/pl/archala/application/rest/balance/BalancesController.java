package pl.archala.application.rest.balance;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.archala.application.command.balance.create.CreateBalanceApplicationService;
import pl.archala.application.command.balance.create.CreateBalanceCommand;
import pl.archala.application.command.balance.create.CreateBalanceResult;
import pl.archala.application.command.balance.send_money.SendMoneyApplicationService;
import pl.archala.application.command.balance.send_money.SendMoneyCommand;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/balances")
public class BalancesController {

    private final CreateBalanceApplicationService createBalanceApplicationService;
    private final SendMoneyApplicationService sendMoneyApplicationService;

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping
    public CreateBalanceResult create(@RequestParam RestCreateBalanceRequest request, Principal principal) {
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
    @PostMapping("/transaction")
    public void sendMoney(@Valid @RequestBody RestSendMoneyRequest request, Principal principal) {
        var command = new SendMoneyCommand(request.sourceBalanceId(),
                                           request.targetBalanceId(),
                                           request.amount(),
                                           principal.getName());
        sendMoneyApplicationService.sendMoney(command);
        log.info("Money has been sent from balance with id {} to balance with id {} with amount: {}",
                 request.sourceBalanceId(),
                 request.targetBalanceId(),
                 request.amount());
    }
}
