package ru.kostrykinmark.user.mapper;

import org.springframework.stereotype.Component;
import ru.kostrykinmark.user.dto.UserDto;
import ru.kostrykinmark.user.model.User;

@Component
public class UserMapper {

    public UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public User mapToUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }
}
