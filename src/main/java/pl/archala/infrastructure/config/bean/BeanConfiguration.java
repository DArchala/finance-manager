package pl.archala.infrastructure.config.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.archala.application.api.error.ApplicationException;
import pl.archala.application.command.balance.create.CreateBalanceApplicationService;
import pl.archala.application.command.balance.send_money.SendMoneyApplicationService;
import pl.archala.application.command.user.create.CreateUserApplicationService;
import pl.archala.application.command.user.notify.NotifyUser;
import pl.archala.domain.balance.GenerateBalanceIdentifier;
import pl.archala.domain.balance.BalanceRepositoryPort;
import pl.archala.domain.user.EncodePassword;
import pl.archala.domain.user.UserRepositoryPort;
import pl.archala.infrastructure.adapter.in.encode.UserPasswordEncoder;
import pl.archala.infrastructure.adapter.in.notify.NotifyUserService;
import pl.archala.infrastructure.adapter.in.scheduling.TransactionsScheduler;
import pl.archala.infrastructure.adapter.in.generate.BalanceIdentifierGenerator;
import pl.archala.infrastructure.adapter.out.persistence.balance.PostgresBalanceRepository;
import pl.archala.infrastructure.adapter.out.persistence.balance.JpaBalanceRepository;
import pl.archala.infrastructure.adapter.out.persistence.user.JpaUserRepository;
import pl.archala.infrastructure.adapter.out.persistence.user.PostgresUserRepository;
import pl.archala.infrastructure.config.security.SecurityUserDetails;
import pl.archala.shared.TransactionExecutor;

import java.security.SecureRandom;

@Configuration
class BeanConfiguration {

    @Bean
    SecureRandom secureRandom() {
        return new SecureRandom();
    }

    @Bean
    BalanceIdentifierGenerator balanceIdentifierGenerator(SecureRandom secureRandom) {
        return new BalanceIdentifierGenerator(secureRandom);
    }

    @Bean
    PostgresBalanceRepository balanceRepository(JpaBalanceRepository jpaBalanceRepository) {
        return new PostgresBalanceRepository(jpaBalanceRepository);
    }

    @Bean
    PostgresUserRepository userRepository(JpaUserRepository jpaUserRepository) {
        return new PostgresUserRepository(jpaUserRepository);
    }

    @Bean
    TransactionsScheduler transactionsScheduler(PostgresBalanceRepository postgresBalanceRepository) {
        return new TransactionsScheduler(postgresBalanceRepository);
    }

    @Bean
    SendMoneyApplicationService sendMoneyApplicationService(UserRepositoryPort userRepositoryPort,
                                                            BalanceRepositoryPort balanceRepositoryPort,
                                                            NotifyUser notifyUser,
                                                            TransactionExecutor transactionExecutor) {
        return new SendMoneyApplicationService(userRepositoryPort,
                                               balanceRepositoryPort,
                                               notifyUser,
                                               transactionExecutor);
    }

    @Bean
    CreateBalanceApplicationService createBalanceApplicationService(UserRepositoryPort userRepositoryPort,
                                                                    BalanceRepositoryPort balanceRepositoryPort,
                                                                    TransactionExecutor transactionExecutor,
                                                                    GenerateBalanceIdentifier generateBalanceIdentifier) {
        return new CreateBalanceApplicationService(userRepositoryPort,
                                                   balanceRepositoryPort,
                                                   transactionExecutor,
                                                   generateBalanceIdentifier);
    }

    @Bean
    NotifyUser notifyUser() {
        return new NotifyUserService();
    }

    @Bean
    CreateUserApplicationService createUserApplicationService(EncodePassword encodePassword,
                                                              TransactionExecutor transactionExecutor,
                                                              UserRepositoryPort userRepositoryPort) {
        return new CreateUserApplicationService(encodePassword,
                                                transactionExecutor,
                                                userRepositoryPort);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    EncodePassword userPasswordEncoder(PasswordEncoder passwordEncoder) {
        return new UserPasswordEncoder(passwordEncoder);
    }

    @Bean
    UserDetailsService userDetailsService(JpaUserRepository userRepository) {
        return name -> userRepository.findByName(name)
                                     .map(SecurityUserDetails::new)
                                     .orElseThrow(() -> ApplicationException.notFound("User with name: %s, not found".formatted(name)));
    }

}
