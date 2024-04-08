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
class UsersValidatorImpl implements UsersValidator {

    private final UsersRepository usersRepository;

    @Override
    public void validateUsersConflicts(AddUserDTO addUserDTO) throws UsersConflictException {
        findByOrThrowException(() -> usersRepository.findUserByUsername(addUserDTO.username()), usernameIsAlreadyTaken(addUserDTO.username()));
        findByOrThrowException(() -> usersRepository.findUserByEmail(addUserDTO.email()), emailIsAlreadyTaken(addUserDTO.email()));
        findByOrThrowException(() -> usersRepository.findUserByPhone(addUserDTO.phone()), phoneIsAlreadyTaken(addUserDTO.phone()));
    }

    @Override
    public void validateUserBeforeTransaction(String username, String sourceBalanceId) throws UserException {
        User user = usersRepository.findUserByUsername(username).orElseThrow(() -> new EntityNotFoundException(userWithUsernameDoesNotExist(username)));
        if (user.getBalance() == null) {
            throw new UserException(userDoesNotHaveBalance(username));
        }
        if (!user.getBalance().getId().equals(sourceBalanceId)) {
            throw new UserException(invalidSourceBalance(sourceBalanceId));
        }
    }

    private void findByOrThrowException(Supplier<Optional<User>> findUserBy, String exceptionMsg) throws UsersConflictException {
        if (findUserBy.get().isPresent()) {
            throw new UsersConflictException(exceptionMsg);
        }
    }
}
