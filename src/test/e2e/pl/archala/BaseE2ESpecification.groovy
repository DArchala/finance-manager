package pl.archala

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.PostgreSQLContainer
import pl.archala.infrastructure.adapter.out.persistence.balance.JpaBalanceRepository
import pl.archala.infrastructure.adapter.out.persistence.user.JpaUserRepository
import spock.lang.Specification

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
abstract class BaseE2ESpecification extends Specification {

    @Autowired
    WebTestClient webTestClient

    @Autowired
    PasswordEncoder passwordEncoder

    @Autowired
    JpaBalanceRepository balanceRepository

    @Autowired
    JpaUserRepository userRepository

    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test")

    static {
        postgres.start()
    }

    @DynamicPropertySource
    static void registerDataSourceProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", { postgres.getJdbcUrl() })
        registry.add("spring.datasource.username", { postgres.getUsername() })
        registry.add("spring.datasource.password", { postgres.getPassword() })
    }


}
