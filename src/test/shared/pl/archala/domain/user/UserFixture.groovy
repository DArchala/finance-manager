package pl.archala.domain.user


import pl.archala.domain.balance.Balance
import pl.archala.domain.notification.NotificationChannel
import pl.archala.infrastructure.adapter.in.encode.UserPasswordEncoder

class UserFixture {

    static User custom(Map map = [:], UserPasswordEncoder userPasswordEncoder) {
        def id = (map.id ?: null) as Long
        def name = (map.name ?: "name") as String
        def password = (map.password ?: userPasswordEncoder.encode("password")) as char[]
        def phone = (map.phone ?: "123123123") as String
        def email = (map.email ?: "user@home.com") as String
        def notificationChannel = (map.notificationChannel ?: NotificationChannel.EMAIL) as NotificationChannel
        def balance = (map.balance ?: null) as Balance
        def externalUuid = (map.externalUuid ?: UUID.randomUUID()) as UUID
        def version = (map.version ?: 0L) as Long
        return new User(id,
                        name,
                        password,
                        phone,
                        email,
                        notificationChannel,
                        balance,
                        externalUuid,
                        version)
    }

}
