package ru.practicum.shareit.item.controller;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.CommonConstants;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoExtendsResp;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;
    private static final String NEGATIVE_ITEM_ID_MESSAGE = "id вещи не может быть отрицательным";

    @PostMapping
    public ItemDto createItem(@PositiveOrZero(message = CommonConstants.NEGATIVE_USER_ID_MESSAGE)
                              @RequestHeader(CommonConstants.USER_ID_HEADER) long userId,
                              @Validated @RequestBody ItemDto itemDto) {
        log.info("Поступил запрос на создание вещи от пользователя с id: {}, с телом: {}", userId, itemDto);
        return itemService.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PositiveOrZero(message = CommonConstants.NEGATIVE_USER_ID_MESSAGE)
                              @RequestHeader(CommonConstants.USER_ID_HEADER) long userId,
                              @PositiveOrZero(message = NEGATIVE_ITEM_ID_MESSAGE)
                              @PathVariable long itemId,
                              @RequestBody ItemDto itemDto) {
        log.info("Поступил запрос на обновление вещи с id: {}, от пользователя с id: {}, с телом: {}", itemId, userId,
                itemDto);
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDtoExtendsResp getItemDtoById(@PositiveOrZero(message = NEGATIVE_ITEM_ID_MESSAGE)
                                             @PathVariable long itemId) {
        log.info("Поступил запрос на получение вещи по id: {}", itemId);
        return itemService.getItemDtoById(itemId);
    }

    @GetMapping
    public List<ItemDtoExtendsResp> getListItemDtoByUserId(@PositiveOrZero(message = CommonConstants.NEGATIVE_USER_ID_MESSAGE)
                                                           @RequestHeader(CommonConstants.USER_ID_HEADER) long userId) {
        log.info("Поступил запрос на получение списка вещей пользователя с id: {}", userId);
        return itemService.getListItemDtoByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getListItemDtoBySubstring(@RequestParam String text) {
        log.info("Поступил запрос на поиск вещей по подстроке: {}", text);
        if (text.isBlank()) {
            return List.of();
        }
        return itemService.getListItemDtoBySubstring(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PositiveOrZero(message = CommonConstants.NEGATIVE_USER_ID_MESSAGE)
                                 @RequestHeader(CommonConstants.USER_ID_HEADER) long userId,
                                 @PositiveOrZero(message = NEGATIVE_ITEM_ID_MESSAGE)
                                 @PathVariable long itemId,
                                 @RequestBody CommentDto commentDto) {
        log.info("Поступил запрос на добавление комментария от пользователя с id: {} для вещи с id: {} с телом: {}",
                userId, itemId, commentDto);
        return itemService.addComment(userId, itemId, commentDto);
    }
}
