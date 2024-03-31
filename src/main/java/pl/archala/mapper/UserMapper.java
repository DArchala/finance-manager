package pl.archala.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.archala.dto.user.AddUserDTO;
import pl.archala.dto.user.GetUserDTO;
import pl.archala.dto.user.UserNotificationData;
import pl.archala.entity.User;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public User toEntity(AddUserDTO addUserDTO) {
        User user = new User();
        user.setUsername(addUserDTO.username());
        user.setPassword(passwordEncoder.encode(addUserDTO.password()).toCharArray());
        user.setPhone(addUserDTO.phone());
        user.setEmail(addUserDTO.email());
        user.setNotificationChannel(addUserDTO.notificationChannel());
        return user;
    }

    public GetUserDTO toGetDto(User user) {
        return new GetUserDTO(user.getId(), user.getUsername(), user.getPhone(), user.getEmail(), user.getNotificationChannel());
    }

    public UserNotificationData toUserNotificationDTO(User user) {
        return new UserNotificationData(user.getNotificationChannel(), user.getPhone(), user.getEmail());
    }
}
