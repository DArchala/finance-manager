package pl.archala.infrastructure.adapter.out.persistance.user;

import pl.archala.application.query.find_user_details.FindUserDetails;
import pl.archala.application.query.find_user_details.FindUserDetailsQuery;
import pl.archala.application.query.find_user_details.FindUserDetailsView;
import pl.archala.domain.user.UserRepositoryInterface;
import pl.archala.domain.user.User;
import pl.archala.application.api.error.ApplicationException;

public record UserRepository(PostgresUserRepository postgresUserRepository) implements UserRepositoryInterface,
                                                                                       FindUserDetails {

    public User persistNew(User user) {
        return postgresUserRepository.persist(user);
    }

    public User findUserByUsername(String username) {
        return postgresUserRepository.findUserByUsername(username)
                                     .orElseThrow(() -> ApplicationException.notFound("User with username: %s, not found".formatted(username)));
    }

    @Override
    public FindUserDetailsView findUserDetails(FindUserDetailsQuery filter) {
        var user = postgresUserRepository.findUserByUsername(filter.username())
                                         .orElseThrow(() -> ApplicationException.notFound("User with username: %s, not found".formatted(filter.username())));

        return new FindUserDetailsView(user.getUsername(),
                                       user.getPhone(),
                                       user.getEmail());
    }
}
