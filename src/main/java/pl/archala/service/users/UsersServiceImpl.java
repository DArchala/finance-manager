package pl.archala.service.users;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.archala.dto.user.AddUserDTO;
import pl.archala.dto.user.GetUserDTO;
import pl.archala.dto.user.UserNotificationData;
import pl.archala.entity.User;
import pl.archala.exception.UsersConflictException;
import pl.archala.mapper.UserMapper;
import pl.archala.repository.UsersRepository;

import static pl.archala.utils.ExceptionInfoProvider.USER_WITH_USERNAME_DOES_NOT_EXIST;

@Service
@RequiredArgsConstructor
@Transactional
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final UsersValidator usersValidator;
    private final UserMapper userMapper;

    @Override
    public GetUserDTO registerUser(AddUserDTO addUserDTO) throws UsersConflictException {
        usersValidator.validateUsersConflicts(addUserDTO);
        User savedUser = usersRepository.save(userMapper.toEntity(addUserDTO));
        return userMapper.toGetDto(savedUser);
    }

    @Override
    public UserNotificationData getUserNotificationData(String username) {
        return userMapper.toUserNotificationDTO(usersRepository.findUserByUsername(username).orElseThrow(
                () -> new EntityNotFoundException(USER_WITH_USERNAME_DOES_NOT_EXIST.formatted(username))
        ));
    }

}
