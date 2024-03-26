package pl.archala.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.archala.dto.user.AddUserDTO;
import pl.archala.dto.user.GetUserDTO;
import pl.archala.entity.User;
import pl.archala.mapper.UserMapper;
import pl.archala.repository.UsersRepository;
import pl.archala.service.UsersService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final UserMapper userMapper;

    @Override
    public GetUserDTO findById(Long id) {
        return userMapper.toGetDto(findUserById(id));
    }

    @Override
    public GetUserDTO registerUser(AddUserDTO addUserDTO) {
        User savedUser = usersRepository.save(userMapper.toEntity(addUserDTO));
        return userMapper.toGetDto(savedUser);
    }

    private User findUserById(Long id) {
        Optional<User> optionalUser = usersRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("User with id %d does not exist".formatted(id));
        }
        return optionalUser.get();
    }

}
