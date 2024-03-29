package pl.archala.service.users;

import pl.archala.dto.user.AddUserDTO;
import pl.archala.dto.user.GetUserDTO;
import pl.archala.dto.user.UserNotificationData;
import pl.archala.exception.UsersConflictException;

public interface UsersService {

    GetUserDTO registerUser(AddUserDTO addUserDTO) throws UsersConflictException;

    UserNotificationData getUserNotificationData(String username);
}
