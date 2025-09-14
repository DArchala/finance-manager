package pl.archala.infrastructure.adapter.out.persistence.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import pl.archala.application.query.find_user_details.FindUserDetails;
import pl.archala.application.query.find_user_details.FindUserDetailsQuery;
import pl.archala.application.query.find_user_details.FindUserDetailsView;
import pl.archala.domain.user.UserRepositoryPort;
import pl.archala.domain.user.User;
import pl.archala.application.api.error.ApplicationException;

@Slf4j
@RequiredArgsConstructor
public class PostgresUserRepository implements UserRepositoryPort, FindUserDetails {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public void persistNew(User user) {
        try {
            jpaUserRepository.persist(user);
        } catch (DataIntegrityViolationException e) {
            log.info("User with name: {} already exists", user.getName());
            log.info("DataIntegrityViolationException: ", e);
            var message = e.getMostSpecificCause()
                           .getMessage();
            if (message.contains("users_name_key")) {
                throw ApplicationException.badRequest("User name: %s is already taken".formatted(user.getName()));
            }
            else if (message.contains("users_phone_key")) {
                throw ApplicationException.badRequest("User phone: %s is already taken".formatted(user.getPhone()));
            }
            else if (message.contains("users_email_key")) {
                throw ApplicationException.badRequest("User email: %s is already taken".formatted(user.getEmail()));
            }
            else {
                throw ApplicationException.internalError();
            }
        }
    }

    @Override
    public User findByName(String name) {
        return jpaUserRepository.findByName(name)
                                .orElseThrow(() -> ApplicationException.notFound("User with name: %s, not found".formatted(name)));
    }

    @Override
    public FindUserDetailsView findUserDetails(FindUserDetailsQuery filter) {
        var user = jpaUserRepository.findByName(filter.name())
                                    .orElseThrow(() -> ApplicationException.notFound("User with name: %s, not found".formatted(filter.name())));

        return new FindUserDetailsView(user.getName(),
                                       user.getPhone(),
                                       user.getEmail(),
                                       user.getNotificationChannel());
    }
}
