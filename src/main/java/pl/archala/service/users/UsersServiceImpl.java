package pl.archala.service.users;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.archala.dto.user.AddUserDTO;
import pl.archala.dto.user.GetUserDTO;
import pl.archala.dto.user.UserNotificationData;
import pl.archala.entity.User;
import pl.archala.exception.UsersConflictException;
import pl.archala.mapper.UsersMapper;
import pl.archala.repository.UsersRepository;

import static pl.archala.utils.ExceptionInfoProvider.USER_WITH_USERNAME_DOES_NOT_EXIST;

@Service
@RequiredArgsConstructor
@Transactional
class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final UsersValidator usersValidator;
    private final UsersMapper usersMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public GetUserDTO registerUser(AddUserDTO addUserDTO) throws UsersConflictException {
        usersValidator.validateUsersConflicts(addUserDTO);
        User newUser = usersMapper.toEntity(addUserDTO);
        newUser.setPassword(passwordEncoder.encode(addUserDTO.password()).toCharArray());
        User savedUser = usersRepository.save(newUser);
        return usersMapper.toGetDto(savedUser);
    }

    @Override
    public UserNotificationData getUserNotificationData(String username) {
        return usersMapper.toUserNotificationDTO(usersRepository.findUserByUsername(username).orElseThrow(
                () -> new EntityNotFoundException(USER_WITH_USERNAME_DOES_NOT_EXIST.formatted(username))
        ));
    }

}
