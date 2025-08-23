package pl.archala.application.rest.balance;

import pl.archala.domain.balance.BalanceCode;

public record RestCreateBalanceRequest(BalanceCode balanceCode) {
}
