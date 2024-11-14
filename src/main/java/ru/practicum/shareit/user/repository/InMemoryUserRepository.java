package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private long currentId;

    @Override
    public User addUser(User user) {
        long id = getCurrentId();
        user.setId(id);
        users.put(id, user);
        emails.add(user.getEmail());
        return user;
    }

    @Override
    public User updateUser(User user) {
        User oldUser = users.put(user.getId(), user);
        emails.remove(oldUser.getEmail());
        emails.add(user.getEmail());
        return user;
    }

    @Override
    public Optional<User> getUserById(long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public boolean isUsedEmail(String email) {
        return emails.contains(email);
    }

    @Override
    public void deleteUser(long userId) {
        users.remove(userId);
    }

    private long getCurrentId() {
        return currentId++;
    }
}
