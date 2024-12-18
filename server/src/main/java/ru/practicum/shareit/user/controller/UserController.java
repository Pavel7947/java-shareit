package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;


@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto addUser(@RequestBody UserDto userDto) {
        log.info("Поступил запрос на добавление пользователя с телом: {}", userDto);
        return userService.addUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable long userId, @RequestBody UserDto userDto) {
        log.info("Поступил запрос на обновление пользователя по id: {}, с телом :{}", userId, userDto);
        return userService.updateUser(userId, userDto);
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable long userId) {
        log.info("Поступил запрос на получение пользователя по id: {}", userId);
        return userService.getUserDtoById(userId);
    }


    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("Поступил запрос на удаление пользователя по id: {}", userId);
        userService.deleteUser(userId);
    }
}
