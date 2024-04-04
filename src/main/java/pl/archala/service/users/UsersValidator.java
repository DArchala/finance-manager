package pl.archala.service.users;

import pl.archala.dto.user.AddUserDTO;
import pl.archala.exception.UserException;
import pl.archala.exception.UsersConflictException;

public interface UsersValidator {

    void validateUsersConflicts(AddUserDTO addUserDTO) throws UsersConflictException;
    void validateUserBeforeTransaction(String username, String sourceBalanceId) throws UserException;

}
