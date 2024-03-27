package pl.archala.service;

import pl.archala.dto.user.AddUserDTO;
import pl.archala.dto.user.GetUserDTO;
import pl.archala.exception.UsersConflictException;

public interface UsersService {

    GetUserDTO registerUser(AddUserDTO addUserDTO) throws UsersConflictException;
}
