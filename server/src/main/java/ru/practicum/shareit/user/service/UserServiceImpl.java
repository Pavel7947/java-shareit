package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.common.exception.ConflictException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDto addUser(UserDto userDto) {
        checkEmailUnique(userDto);
        User addedUser = UserDtoMapper.toUser(userDto);
        return UserDtoMapper.toUserDto(userRepository.save(addedUser));
    }

    @Transactional
    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        User user = getUserById(userId);
        checkEmailUnique(userDto);
        String email = userDto.getEmail();
        if (email != null && !email.isBlank()) {
            user.setEmail(email);
        }
        String name = userDto.getName();
        if (name != null && !name.isBlank()) {
            user.setName(name);
        }
        return UserDtoMapper.toUserDto(user);
    }


    @Override
    public UserDto getUserDtoById(long userId) {
        return UserDtoMapper.toUserDto(getUserById(userId));
    }

    @Transactional
    @Override
    public void deleteUser(long userId) {
        getUserById(userId);
        userRepository.deleteById(userId);
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден"));
    }

    private void checkEmailUnique(UserDto userDto) {
        String email = userDto.getEmail();
        if (email != null && !email.isBlank()) {
            if (userRepository.existsByEmail(email)) {
                throw new ConflictException("Переданный email уже занят другим пользователем");
            }
        }
    }
}
