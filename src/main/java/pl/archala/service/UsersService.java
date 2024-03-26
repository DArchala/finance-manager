package pl.archala.service;

import pl.archala.dto.user.AddUserDTO;
import pl.archala.dto.user.GetUserDTO;

public interface UsersService {

    GetUserDTO findById(Long id);

    GetUserDTO registerUser(AddUserDTO addUserDTO);
}
