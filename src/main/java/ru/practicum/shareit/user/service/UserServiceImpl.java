package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
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
        User addedUser = UserDtoMapper.toUser(userDto);
        checkEmailUnique(addedUser);
        return UserDtoMapper.toUserDto(userRepository.save(addedUser));
    }

    @Transactional
    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        User oldUser = getUserById(userId);
        User updatedUser = UserDtoMapper.toUser(userDto);
        updatedUser.setId(userId);
        checkEmailUnique(updatedUser);
        String email = updatedUser.getEmail();
        if (email == null || email.isBlank()) {
            updatedUser.setEmail(oldUser.getEmail());
        }
        String name = updatedUser.getName();
        if (name == null || name.isBlank()) {
            updatedUser.setName(oldUser.getName());
        }
        return UserDtoMapper.toUserDto(userRepository.save(updatedUser));
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

    private void checkEmailUnique(User user) {
        String email = user.getEmail();
        if (email != null && !email.isBlank()) {
            if (userRepository.existsByEmail(email)) {
                throw new ConflictException("Переданный email уже занят другим пользователем");
            }
        }
    }
}
