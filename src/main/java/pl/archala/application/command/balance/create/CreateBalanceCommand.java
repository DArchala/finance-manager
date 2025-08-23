package pl.archala.application.command.balance.create;

import pl.archala.domain.balance.BalanceCode;

public record CreateBalanceCommand(BalanceCode balanceCode, String username) {
}
