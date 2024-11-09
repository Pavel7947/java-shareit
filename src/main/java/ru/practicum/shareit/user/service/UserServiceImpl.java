package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    @Override
    public UserDto addUser(UserDto userDto) {
        User addedUser = userDtoMapper.toUser(userDto);
        checkEmailUnique(addedUser);
        return userDtoMapper.toUserDto(userRepository.addUser(addedUser));
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        User oldUser = getUserById(userId);
        User updatedUser = userDtoMapper.toUser(userDto);
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
        return userDtoMapper.toUserDto(userRepository.updateUser(updatedUser));
    }

    @Override
    public UserDto getUserDtoById(long userId) {
        return userDtoMapper.toUserDto(getUserById(userId));
    }

    @Override
    public void deleteUser(long userId) {
        getUserById(userId);
        userRepository.deleteUser(userId);
    }

    private User getUserById(long userId) {
        return userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден"));
    }

    /**
     * This method checks the uniqueness of the email.
     * The verification method uses the values of the fields of the passed entity.
     * A user with an initialized id field is passed for verification.
     * This is very important for correct verification.
     * Since the identity of the email address is determined by the id.
     *
     * @throws ConflictException if the uniqueness check fails
     */
    private void checkEmailUnique(User user) {
        String email = user.getEmail();
        if (email != null && !email.isBlank()) {
            Optional<User> foundUser = userRepository.getUserByEmail(email);
            if (foundUser.isPresent() && foundUser.get().getId() != user.getId()) {
                throw new ConflictException("Переданный email уже занят другим пользователем");
            }
        }
    }
}
