package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserRepository {

    User addUser(User user);

    User updateUser(User user);

    Optional<User> getUserById(long userId);

    boolean isUsedEmail(String email);

    void deleteUser(long userId);
}
