package pl.archala.service;

import pl.archala.dto.user.AddUserDTO;
import pl.archala.exception.UsersConflictException;

public interface UsersValidator {

    void validateUserConflicts(AddUserDTO addUserDTO) throws UsersConflictException;

}
