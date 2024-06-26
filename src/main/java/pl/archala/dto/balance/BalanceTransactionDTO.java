package pl.archala.dto.balance;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

public record BalanceTransactionDTO(@RequestParam String sourceBalanceId,
                                    @RequestParam String targetBalanceId,
                                    @RequestParam @DecimalMin(value = "0.0", inclusive = false,
                                            message = "Value to transact must be bigger than 0")
                                    @Digits(integer = 10, fraction = 2, message = "Value must contains max 10 digits before comma and max 2 after.") BigDecimal value) {
}
