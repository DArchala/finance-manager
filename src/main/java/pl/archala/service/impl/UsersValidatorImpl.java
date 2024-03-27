package pl.archala.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.archala.dto.user.AddUserDTO;
import pl.archala.exception.UsersConflictException;
import pl.archala.repository.UsersRepository;
import pl.archala.service.UsersValidator;

import static pl.archala.utils.StringInfoProvider.*;

@Service
@RequiredArgsConstructor
public class UsersValidatorImpl implements UsersValidator {

    private final UsersRepository usersRepository;

    @Override
    public void validateUserConflicts(AddUserDTO addUserDTO) throws UsersConflictException {
        validateUserNameConflicts(addUserDTO);
        validateUserEmailConflicts(addUserDTO);
        validateUserPhoneConflicts(addUserDTO);
    }

    private void validateUserNameConflicts(AddUserDTO addUserDTO) throws UsersConflictException {
        if (usersRepository.findUserByUsername(addUserDTO.username()).isPresent()) {
            throw new UsersConflictException(USERNAME_IS_ALREADY_TAKEN.formatted(addUserDTO.username()));
        }
    }

    private void validateUserEmailConflicts(AddUserDTO addUserDTO) throws UsersConflictException {
        if (usersRepository.findUserByEmail(addUserDTO.email()).isPresent()) {
            throw new UsersConflictException(EMAIL_IS_ALREADY_TAKEN.formatted(addUserDTO.email()));
        }
    }

    private void validateUserPhoneConflicts(AddUserDTO addUserDTO) throws UsersConflictException {
        if (usersRepository.findUserByPhone(addUserDTO.phone()).isPresent()) {
            throw new UsersConflictException(PHONE_IS_ALREADY_TAKEN.formatted(addUserDTO.phone()));
        }
    }
}
