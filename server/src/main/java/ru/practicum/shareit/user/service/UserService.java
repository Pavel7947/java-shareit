package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {

    UserDto addUser(UserDto user);

    UserDto updateUser(long userId, UserDto user);

    UserDto getUserDtoById(long userId);

    void deleteUser(long userId);
}
