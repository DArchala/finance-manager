package pl.archala.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.archala.enums.NotificationChannel;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Table(name = "users")
@Entity
public class User implements UserDetails {

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

    @EqualsAndHashCode.Exclude
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "balance_id")
    private Balance balance;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return new String(password);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof User user)) return false;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Arrays.equals(password, user.password) && Objects.equals(phone, user.phone) && Objects.equals(email, user.email) && notificationChannel == user.notificationChannel;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, username, phone, email, notificationChannel);
        result = 31 * result + Arrays.hashCode(password);
        return result;
    }
}
