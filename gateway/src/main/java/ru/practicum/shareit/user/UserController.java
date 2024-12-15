package ru.practicum.shareit.user;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.CommonConstants;
import ru.practicum.shareit.common.validation.ValidationAtCreate;
import ru.practicum.shareit.common.validation.ValidationAtUpdate;
import ru.practicum.shareit.user.dto.UserDto;

@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Controller
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> addUser(@Validated(ValidationAtCreate.class) @RequestBody UserDto userDto) {
        log.info("Поступил запрос на добавление пользователя с телом: {}", userDto);
        return userClient.addUser(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PositiveOrZero(message = CommonConstants.NEGATIVE_USER_ID_MESSAGE)
                                             @PathVariable long userId,
                                             @Validated(ValidationAtUpdate.class) @RequestBody UserDto userDto) {
        log.info("Поступил запрос на обновление пользователя по id: {}, с телом :{}", userId, userDto);
        return userClient.updateUser(userId, userDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PositiveOrZero(message = CommonConstants.NEGATIVE_USER_ID_MESSAGE)
                                          @PathVariable long userId) {
        log.info("Поступил запрос на получение пользователя по id: {}", userId);
        return userClient.getUser(userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PositiveOrZero(message = CommonConstants.NEGATIVE_USER_ID_MESSAGE)
                                             @PathVariable long userId) {
        log.info("Поступил запрос на удаление пользователя по id: {}", userId);
        return userClient.deleteUser(userId);
    }

}
