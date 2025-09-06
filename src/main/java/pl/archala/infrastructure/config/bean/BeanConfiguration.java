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
import pl.archala.application.command.user.notify.NotifyUserApplicationInterface;
import pl.archala.domain.balance.BalanceRepositoryInterface;
import pl.archala.domain.user.UserPasswordEncoderInterface;
import pl.archala.domain.user.UserRepositoryInterface;
import pl.archala.infrastructure.adapter.in.encode.UserPasswordEncoder;
import pl.archala.infrastructure.adapter.in.notify.NotifyUserService;
import pl.archala.infrastructure.adapter.in.scheduling.TransactionsScheduler;
import pl.archala.infrastructure.adapter.out.BalanceIdentifierGenerator;
import pl.archala.infrastructure.adapter.out.persistance.balance.BalanceRepository;
import pl.archala.infrastructure.adapter.out.persistance.balance.PostgresBalanceRepository;
import pl.archala.infrastructure.adapter.out.persistance.user.PostgresUserRepository;
import pl.archala.infrastructure.adapter.out.persistance.user.UserRepository;
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
    BalanceRepository balanceRepository(PostgresBalanceRepository postgresBalanceRepository) {
        return new BalanceRepository(postgresBalanceRepository);
    }

    @Bean
    UserRepository userRepository(PostgresUserRepository postgresUserRepository) {
        return new UserRepository(postgresUserRepository);
    }

    @Bean
    TransactionsScheduler transactionsScheduler(BalanceRepository balanceRepository) {
        return new TransactionsScheduler(balanceRepository);
    }

    @Bean
    SendMoneyApplicationService sendMoneyApplicationService(UserRepositoryInterface userRepository,
                                                            BalanceRepositoryInterface balanceRepository,
                                                            NotifyUserApplicationInterface notifyUserApplicationInterface,
                                                            TransactionExecutor transactionExecutor) {
        return new SendMoneyApplicationService(userRepository,
                                               balanceRepository,
                                               notifyUserApplicationInterface,
                                               transactionExecutor);
    }

    @Bean
    CreateBalanceApplicationService createBalanceApplicationService(UserRepositoryInterface userRepository,
                                                                    BalanceRepositoryInterface balanceRepository,
                                                                    TransactionExecutor transactionExecutor) {
        return new CreateBalanceApplicationService(userRepository,
                                                   balanceRepository,
                                                   transactionExecutor);
    }

    @Bean
    NotifyUserService notifyUserService() {
        return new NotifyUserService();
    }

    @Bean
    CreateUserApplicationService createUserApplicationService(UserPasswordEncoderInterface userPasswordEncoderInterface,
                                                              TransactionExecutor transactionExecutor,
                                                              UserRepositoryInterface userRepository) {
        return new CreateUserApplicationService(userPasswordEncoderInterface,
                                                transactionExecutor,
                                                userRepository);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserPasswordEncoder userPasswordEncoder(PasswordEncoder passwordEncoder) {
        return new UserPasswordEncoder(passwordEncoder);
    }

    @Bean
    UserDetailsService userDetailsService(PostgresUserRepository userRepository) {
        return username -> userRepository.findUserByUsername(username)
                                         .map(SecurityUserDetails::new)
                                         .orElseThrow(() -> ApplicationException.notFound("User with username: %s, not found".formatted(username)));
    }

}
