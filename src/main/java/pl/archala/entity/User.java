package pl.archala.entity;

import jakarta.persistence.*;
import lombok.Data;
import pl.archala.enums.NotificationChannel;

@Data
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "balance_id")
    private Balance balance;

}
