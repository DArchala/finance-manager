package pl.archala.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.archala.dto.user.AddUserDTO;
import pl.archala.dto.user.GetUserDTO;
import pl.archala.dto.user.UserNotificationData;
import pl.archala.entity.User;

@Mapper(componentModel = "spring")
public interface UsersMapper {

     @Mapping(target = "id", ignore = true)
     @Mapping(target = "balance", ignore = true)
     @Mapping(target = "authorities", ignore = true)
     @Mapping(target = "password", ignore = true)
     User toEntity(AddUserDTO addUserDTO);

     GetUserDTO toGetDto(User user);

     UserNotificationData toUserNotificationDTO(User user);

}
