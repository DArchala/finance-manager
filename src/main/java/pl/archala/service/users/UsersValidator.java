package pl.archala.service.users;

import pl.archala.dto.user.AddUserDTO;
import pl.archala.exception.UsersConflictException;

public interface UsersValidator {

    void validateUsersConflicts(AddUserDTO addUserDTO) throws UsersConflictException;

}
