package ru.practicum.shareit.item;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.CommonConstants;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;
    private static final String NEGATIVE_ITEM_ID_MESSAGE = "id вещи не может быть отрицательным";

    @PostMapping
    public ResponseEntity<Object> createItem(@PositiveOrZero(message = CommonConstants.NEGATIVE_USER_ID_MESSAGE)
                                             @RequestHeader(CommonConstants.USER_ID_HEADER) long userId,
                                             @Validated @RequestBody ItemDto itemDto) {
        log.info("Поступил запрос на создание вещи от пользователя с id: {}, с телом: {}", userId, itemDto);
        return itemClient.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PositiveOrZero(message = CommonConstants.NEGATIVE_USER_ID_MESSAGE)
                                             @RequestHeader(CommonConstants.USER_ID_HEADER) long userId,
                                             @PositiveOrZero(message = NEGATIVE_ITEM_ID_MESSAGE)
                                             @PathVariable long itemId,
                                             @RequestBody ItemDto itemDto) {
        log.info("Поступил запрос на обновление вещи с id: {}, от пользователя с id: {}, с телом: {}", itemId, userId,
                itemDto);
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PositiveOrZero(message = NEGATIVE_ITEM_ID_MESSAGE)
                                              @PathVariable long itemId) {
        log.info("Поступил запрос на получение вещи по id: {}", itemId);
        return itemClient.getItemById(itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getListItemByUserId(@PositiveOrZero(message = CommonConstants.NEGATIVE_USER_ID_MESSAGE)
                                                      @RequestHeader(CommonConstants.USER_ID_HEADER) long userId) {
        log.info("Поступил запрос на получение списка вещей пользователя с id: {}", userId);
        return itemClient.getListItemByUserId(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getListItemBySubstring(@RequestParam String text) {
        log.info("Поступил запрос на поиск вещей по подстроке: {}", text);
        if (text.isBlank()) {
            return ResponseEntity.ok(List.of());
        }
        return itemClient.getListItemBySubstring(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PositiveOrZero(message = CommonConstants.NEGATIVE_USER_ID_MESSAGE)
                                             @RequestHeader(CommonConstants.USER_ID_HEADER) long userId,
                                             @PositiveOrZero(message = NEGATIVE_ITEM_ID_MESSAGE)
                                             @PathVariable long itemId,
                                             @RequestBody CommentDto commentDto) {
        log.info("Поступил запрос на добавление комментария от пользователя с id: {} для вещи с id: {} с телом: {}",
                userId, itemId, commentDto);
        return itemClient.addComment(userId, itemId, commentDto);
    }

}
