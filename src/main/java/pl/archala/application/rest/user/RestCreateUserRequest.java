package pl.archala.application.rest.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import pl.archala.domain.notification.NotificationChannel;

public record RestCreateUserRequest(@NotBlank(message = "Username must not be blank")
                                    @Size(min = 6, max = 30, message = "Username must contains min 6 and max 30 chars")
                                    String username,

                                    @NotBlank(message = "User password must not be blank")
                                    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,30}$",
                                             message = "Password should contains min 8 and max 30 chars including capital letter, digit and special char (@#$%^&+=)")
                                    String password,

                                    @NotBlank(message = "User phone number must not be blank")
                                    @Pattern(regexp = "^\\d{9}$", message = "User phone number must contains exactly 9 digits")
                                    String phone,

                                    @NotBlank(message = "User e-mail must not be blank")
                                    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
                                             message = "User e-mail must be a well-formed email address") String email,

                                    @NotNull(message = "User notification channel must not be null") NotificationChannel notificationChannel) {
}
