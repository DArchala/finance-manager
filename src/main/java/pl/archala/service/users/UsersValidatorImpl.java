package pl.archala.service.users;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.archala.dto.user.AddUserDTO;
import pl.archala.entity.User;
import pl.archala.exception.UserException;
import pl.archala.exception.UsersConflictException;
import pl.archala.repository.UsersRepository;

import java.util.Optional;
import java.util.function.Supplier;

import static pl.archala.utils.ExceptionInfoProvider.*;

@Service
@RequiredArgsConstructor
public class UsersValidatorImpl implements UsersValidator {

    private final UsersRepository usersRepository;

    @Override
    public void validateUsersConflicts(AddUserDTO addUserDTO) throws UsersConflictException {
        findByOrThrowException(() -> usersRepository.findUserByUsername(addUserDTO.username()), USERNAME_IS_ALREADY_TAKEN.formatted(addUserDTO.username()));
        findByOrThrowException(() -> usersRepository.findUserByEmail(addUserDTO.email()), EMAIL_IS_ALREADY_TAKEN.formatted(addUserDTO.email()));
        findByOrThrowException(() -> usersRepository.findUserByPhone(addUserDTO.phone()), PHONE_IS_ALREADY_TAKEN.formatted(addUserDTO.phone()));
    }

    @Override
    public void validateUserBeforeTransaction(String username, String sourceBalanceId) throws UserException {
        User user = usersRepository.findUserByUsername(username).orElseThrow(() -> new EntityNotFoundException(USER_WITH_USERNAME_DOES_NOT_EXIST.formatted(username)));
        if (user.getBalance() == null) {
            throw new UserException(USER_DOES_NOT_HAVE_BALANCE.formatted(username));
        }
        if (!user.getBalance().getId().equals(sourceBalanceId)) {
            throw new UserException(INVALID_SOURCE_BALANCE.formatted(sourceBalanceId));
        }
    }

    private void findByOrThrowException(Supplier<Optional<User>> findUserBy, String exceptionMsg) throws UsersConflictException {
        if (findUserBy.get().isPresent()) {
            throw new UsersConflictException(exceptionMsg);
        }
    }
}
