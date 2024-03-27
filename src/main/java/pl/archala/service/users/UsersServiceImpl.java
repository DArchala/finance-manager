package pl.archala.service.users;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.archala.dto.user.AddUserDTO;
import pl.archala.dto.user.GetUserDTO;
import pl.archala.entity.User;
import pl.archala.exception.UsersConflictException;
import pl.archala.mapper.UserMapper;
import pl.archala.repository.UsersRepository;
import pl.archala.service.users.UsersService;
import pl.archala.service.users.UsersValidator;

@Service
@RequiredArgsConstructor
@Transactional
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final UsersValidator usersValidator;
    private final UserMapper userMapper;

    @Override
    public GetUserDTO registerUser(AddUserDTO addUserDTO) throws UsersConflictException {
        usersValidator.validateUserConflicts(addUserDTO);
        User savedUser = usersRepository.save(userMapper.toEntity(addUserDTO));
        return userMapper.toGetDto(savedUser);
    }

}
