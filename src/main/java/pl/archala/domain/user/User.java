package pl.archala.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.archala.domain.balance.Balance;
import pl.archala.domain.notification.NotificationChannel;

import java.util.Arrays;
import java.util.Objects;

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
    private String name;

    @Column(nullable = false)
    private char[] password;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private NotificationChannel notificationChannel;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "balance_id", referencedColumnName = "id")
    private Balance balance;

    @Column(nullable = false)
    @Version
    private Long version;

    public static User create(String name, char[] password, String phone, String email, NotificationChannel notificationChannel) {
        return new User(null,
                        name,
                        password,
                        phone,
                        email,
                        notificationChannel,
                        null,
                        0L);
    }

    public void updateBalance(Balance balance) {
        this.balance = balance;
    }

    public boolean hasBalance() {
        return balance != null;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) {
            return false;
        }
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.deepEquals(password, user.password) &&
               Objects.equals(phone, user.phone) && Objects.equals(email, user.email) && notificationChannel == user.notificationChannel &&
               Objects.equals(balance, user.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, Arrays.hashCode(password), phone, email, notificationChannel, balance);
    }
}
