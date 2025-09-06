package pl.archala.domain.user;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.archala.domain.balance.Balance;
import pl.archala.domain.notification.NotificationChannel;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private char[] password;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private NotificationChannel notificationChannel;

    @ToString.Exclude
    @Nullable
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "balance_id")
    private Balance balance;

    @Column(nullable = false, unique = true)
    private UUID externalUuid;

    @Version
    private Long version;

    public static User create(String username, char[] password, String phone, String email, NotificationChannel notificationChannel) {
        return new User(null,
                        username,
                        password,
                        phone,
                        email,
                        notificationChannel,
                        null,
                        UUID.randomUUID(),
                        0L);
    }

    public void updateBalance(Balance balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) {
            return false;
        }
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.deepEquals(password, user.password) &&
               Objects.equals(phone, user.phone) && Objects.equals(email, user.email) && notificationChannel == user.notificationChannel &&
               Objects.equals(balance, user.balance) && Objects.equals(externalUuid, user.externalUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, Arrays.hashCode(password), phone, email, notificationChannel, balance, externalUuid);
    }
}
