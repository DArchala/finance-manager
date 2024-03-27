package pl.archala.mapper;

import org.springframework.stereotype.Component;
import pl.archala.dto.user.AddUserDTO;
import pl.archala.dto.user.GetUserDTO;
import pl.archala.entity.User;

@Component
public class UserMapper {

    public GetUserDTO toGetDto(User user) {
        return new GetUserDTO(user.getId(), user.getUsername(), user.getPhone(), user.getEmail(), user.getNotificationChannel());
    }

    public User toEntity(AddUserDTO addUserDTO) {
        User user = new User();
        user.setUsername(addUserDTO.username());
        user.setPassword(addUserDTO.password().toCharArray());
        user.setPhone(addUserDTO.phone());
        user.setEmail(addUserDTO.email());
        user.setNotificationChannel(addUserDTO.notificationChannel());
        return user;
    }
}
