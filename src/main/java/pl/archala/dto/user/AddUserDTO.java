package pl.archala.dto.user;

import jakarta.validation.constraints.*;
import pl.archala.enums.NotificationChannel;

public record AddUserDTO(@NotBlank(message = "Username must not be blank")
                         @Size(min = 6, max = 30, message = "Username must contains min 6 and max 30 chars")
                         String username,

                         @NotBlank(message = "User password must not be blank")
                         @Size(min = 8, max = 20, message = "User password must contains min 8 and max 20 chars")
                         String password,

                         @NotBlank(message = "User phone number must not be blank")
                         @Pattern(regexp = "^\\d{9}$")
                         String phone,

                         @NotBlank(message = "User e-mail must not be blank") @Email String email,

                         @NotNull(message = "User notification channel must not be null") NotificationChannel notificationChannel) {

}
