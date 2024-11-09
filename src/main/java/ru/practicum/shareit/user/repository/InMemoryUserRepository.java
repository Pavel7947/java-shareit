package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private long currentId;

    @Override
    public User addUser(User user) {
        long id = getCurrentId();
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> getUserById(long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return users.values().stream().filter(user -> user.getEmail().equals(email)).findAny();
    }

    @Override
    public void deleteUser(long userId) {
        users.remove(userId);
    }

    private long getCurrentId() {
        return currentId++;
    }
}
