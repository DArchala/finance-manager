package pl.archala.service.users;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.archala.dto.user.AddUserDTO;
import pl.archala.exception.UsersConflictException;
import pl.archala.repository.UsersRepository;

import static pl.archala.utils.ExceptionInfoProvider.*;

@Service
@RequiredArgsConstructor
public class UsersValidatorImpl implements UsersValidator {

    private final UsersRepository usersRepository;

    @Override
    public void validateUserConflicts(AddUserDTO addUserDTO) throws UsersConflictException {
        usersRepository.findUserByUsername(addUserDTO.username()).orElseThrow(() -> new UsersConflictException(USERNAME_IS_ALREADY_TAKEN.formatted(addUserDTO.username())));
        usersRepository.findUserByEmail(addUserDTO.email()).orElseThrow(() -> new UsersConflictException(EMAIL_IS_ALREADY_TAKEN.formatted(addUserDTO.email())));
        usersRepository.findUserByPhone(addUserDTO.phone()).orElseThrow(() -> new UsersConflictException(PHONE_IS_ALREADY_TAKEN.formatted(addUserDTO.phone())));
    }

}
