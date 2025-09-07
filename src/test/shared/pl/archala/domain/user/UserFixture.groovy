package pl.archala.domain.user

import org.springframework.security.crypto.password.PasswordEncoder
import pl.archala.domain.balance.Balance
import pl.archala.domain.notification.NotificationChannel

class UserFixture {

    static User custom(Map map = [:], PasswordEncoder passwordEncoder) {
        def id = (map.id ?: null) as Long
        def username = (map.username ?: "username") as String
        def password = (map.password ?: passwordEncoder.encode("password").toCharArray()) as char[]
        def phone = (map.phone ?: "123123123") as String
        def email = (map.email ?: "user@home.com") as String
        def notificationChannel = (map.notificationChannel ?: NotificationChannel.EMAIL) as NotificationChannel
        def balance = (map.balance ?: null) as Balance
        def externalUuid = (map.externalUuid ?: UUID.randomUUID()) as UUID
        def version = (map.version ?: 0L) as Long
        return new User(id,
                        username,
                        password,
                        phone,
                        email,
                        notificationChannel,
                        balance,
                        externalUuid,
                        version)
    }

}
