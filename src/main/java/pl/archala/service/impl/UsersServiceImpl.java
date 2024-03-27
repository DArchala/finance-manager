package pl.archala.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.archala.dto.user.AddUserDTO;
import pl.archala.dto.user.GetUserDTO;
import pl.archala.entity.User;
import pl.archala.exception.UsersConflictException;
import pl.archala.mapper.UserMapper;
import pl.archala.repository.UsersRepository;
import pl.archala.service.UsersService;

import static pl.archala.utils.StringInfoProvider.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final UserMapper userMapper;

    @Override
    public GetUserDTO registerUser(AddUserDTO addUserDTO) throws UsersConflictException {
        if (usersRepository.findUserByUsername(addUserDTO.username()).isPresent()) {
            throw new UsersConflictException(USERNAME_IS_ALREADY_TAKEN.formatted(addUserDTO.username()));
        }
        if (usersRepository.findUserByEmail(addUserDTO.email()).isPresent()) {
            throw new UsersConflictException(EMAIL_IS_ALREADY_TAKEN.formatted(addUserDTO.email()));
        }
        if (usersRepository.findUserByPhone(addUserDTO.phone()).isPresent()) {
            throw new UsersConflictException(PHONE_IS_ALREADY_TAKEN.formatted(addUserDTO.phone()));
        }

        User savedUser = usersRepository.save(userMapper.toEntity(addUserDTO));
        return userMapper.toGetDto(savedUser);
    }

}
