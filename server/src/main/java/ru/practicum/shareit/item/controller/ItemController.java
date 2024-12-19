package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.CommonConstants;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoExtendsResp;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader(CommonConstants.USER_ID_HEADER) long userId,
                              @RequestBody ItemDto itemDto) {
        log.info("Поступил запрос на создание вещи от пользователя с id: {}, с телом: {}", userId, itemDto);
        return itemService.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(CommonConstants.USER_ID_HEADER) long userId,
                              @PathVariable long itemId, @RequestBody ItemDto itemDto) {
        log.info("Поступил запрос на обновление вещи с id: {}, от пользователя с id: {}, с телом: {}", itemId, userId,
                itemDto);
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDtoExtendsResp getItemDtoById(@PathVariable long itemId) {
        log.info("Поступил запрос на получение вещи по id: {}", itemId);
        return itemService.getItemDtoById(itemId);
    }

    @GetMapping
    public List<ItemDtoExtendsResp> getListItemDtoByUserId(@RequestHeader(CommonConstants.USER_ID_HEADER) long userId) {
        log.info("Поступил запрос на получение списка вещей пользователя с id: {}", userId);
        return itemService.getListItemDtoByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getListItemDtoBySubstring(@RequestParam String text) {
        log.info("Поступил запрос на поиск вещей по подстроке: {}", text);
        return itemService.getListItemDtoBySubstring(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(CommonConstants.USER_ID_HEADER) long userId,
                                 @PathVariable long itemId, @RequestBody CommentDto commentDto) {
        log.info("Поступил запрос на добавление комментария от пользователя с id: {} для вещи с id: {} с телом: {}",
                userId, itemId, commentDto);
        return itemService.addComment(userId, itemId, commentDto);
    }
}
