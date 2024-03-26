package pl.archala.service;

import pl.archala.dto.AddUserDTO;
import pl.archala.dto.GetUserDTO;

public interface UsersService {

    GetUserDTO registerUser(AddUserDTO addUserDTO);
}
