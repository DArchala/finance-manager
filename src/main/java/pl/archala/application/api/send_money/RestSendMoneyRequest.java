package pl.archala.application.api.send_money;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record RestSendMoneyRequest(@NotBlank String targetBalanceId,
                                   @DecimalMin(value = "0.0", inclusive = false,
                                                             message = "Value to transact must be bigger than 0")
                                   @Digits(integer = 10, fraction = 2, message = "Value must contains max 10 digits before comma and max 2 after.") BigDecimal amount) {
}
