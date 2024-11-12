package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto createItem(@RequestHeader(USER_ID_HEADER) long userId, @Validated @RequestBody ItemDto itemDto) {
        log.info("Поступил запрос на создание вещи от пользователя с id: {}, с телом: {}", userId, itemDto);
        return itemService.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(USER_ID_HEADER) long userId, @PathVariable long itemId,
                              @RequestBody ItemDto itemDto) {
        log.info("Поступил запрос на обновление вещи с id: {}, от пользователя с id: {}, с телом: {}", itemId, userId,
                itemDto);
        return itemService.updateItem(itemId, userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemDtoById(@PathVariable long itemId) {
        log.info("Поступил запрос на получение вещи по id: {}", itemId);
        return itemService.getItemDtoById(itemId);
    }

    @GetMapping
    public List<ItemDto> getListItemDtoByUserId(@RequestHeader(USER_ID_HEADER) long userId) {
        log.info("Поступил запрос на получение списка вещей пользователя с id: {}", userId);
        return itemService.getListItemDtoByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getListItemDtoBySubstring(@RequestParam String text) {
        log.info("Поступил запрос на поиск вещей по подстроке: {}", text);
        return itemService.getListItemDtoBySubstring(text);
    }
}
