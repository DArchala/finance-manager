package pl.archala.infrastructure.adapter.out.persistence.user;

import pl.archala.application.query.find_user_details.FindUserDetails;
import pl.archala.application.query.find_user_details.FindUserDetailsQuery;
import pl.archala.application.query.find_user_details.FindUserDetailsView;
import pl.archala.domain.user.UserRepositoryPort;
import pl.archala.domain.user.User;
import pl.archala.application.api.error.ApplicationException;

public record PostgresUserRepository(JpaUserRepository jpaUserRepository) implements UserRepositoryPort,
                                                                                     FindUserDetails {

    @Override
    public User persistNew(User user) {
        return jpaUserRepository.persist(user);
    }

    @Override
    public User findUserByUsername(String username) {
        return jpaUserRepository.findByName(username)
                                .orElseThrow(() -> ApplicationException.notFound("User with username: %s, not found".formatted(username)));
    }

    @Override
    public FindUserDetailsView findUserDetails(FindUserDetailsQuery filter) {
        var user = jpaUserRepository.findByName(filter.username())
                                    .orElseThrow(() -> ApplicationException.notFound("User with username: %s, not found".formatted(filter.username())));

        return new FindUserDetailsView(user.getName(),
                                       user.getPhone(),
                                       user.getEmail());
    }
}
