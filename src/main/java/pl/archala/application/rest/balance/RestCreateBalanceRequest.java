package pl.archala.application.rest.balance;

import jakarta.validation.constraints.NotNull;
import pl.archala.domain.balance.BalanceCode;

public record RestCreateBalanceRequest(@NotNull BalanceCode balanceCode) {
}
