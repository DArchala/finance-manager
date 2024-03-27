package pl.archala.service.users;

import pl.archala.dto.user.AddUserDTO;
import pl.archala.exception.UsersConflictException;

public interface UsersValidator {

    void validateUserConflicts(AddUserDTO addUserDTO) throws UsersConflictException;

}
