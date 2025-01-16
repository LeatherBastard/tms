package ru.kostrykinmark.user.mapper;

import org.springframework.stereotype.Component;
import ru.kostrykinmark.user.dto.UserShortDto;
import ru.kostrykinmark.user.model.User;

@Component
public class UserMapper {
    public UserShortDto mapToUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
