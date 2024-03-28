package pl.archala.service.users;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.archala.dto.user.AddUserDTO;
import pl.archala.entity.User;
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
        findByOrThrowException(() -> usersRepository.findUserByEmail(addUserDTO.username()), EMAIL_IS_ALREADY_TAKEN.formatted(addUserDTO.email()));
        findByOrThrowException(() -> usersRepository.findUserByPhone(addUserDTO.username()), PHONE_IS_ALREADY_TAKEN.formatted(addUserDTO.phone()));
    }

    private void findByOrThrowException(Supplier<Optional<User>> findUserBy, String exceptionMsg) throws UsersConflictException {
        if (findUserBy.get().isPresent()) {
            throw new UsersConflictException(exceptionMsg);
        }
    }
}
