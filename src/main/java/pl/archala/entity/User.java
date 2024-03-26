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

    private char[] password;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String email;

    private NotificationChannel notificationChannel;

}
