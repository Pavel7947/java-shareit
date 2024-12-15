package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.common.exception.NotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplIntTest {
    private final UserServiceImpl userService;
    private static final long USER_ID_EXISTS = 4; // This user exists in database
    private static final long USER_ID_NOT_EXISTS = 0; // This user is not exists in database

    @Test
    void getUserDtoById_whenUserIsExists_ThenReturnUserDto() {
        assertNotNull(userService.getUserDtoById(USER_ID_EXISTS));
    }

    @Test
    void getUserDtoById_whenUserIsNotExists_ThenThrowNotFound() {
        assertThrows(NotFoundException.class, () -> userService.getUserDtoById(USER_ID_NOT_EXISTS));
    }

    @Test
    void deleteUser_whenUserIsExists_ThenNotThrowsException() {
        assertDoesNotThrow(() -> userService.deleteUser(USER_ID_EXISTS));
    }

    @Test
    void deleteUser_whenUserIsNotExists_ThenThrowNotFound() {
        assertThrows(NotFoundException.class, () -> userService.deleteUser(USER_ID_NOT_EXISTS));
    }
}