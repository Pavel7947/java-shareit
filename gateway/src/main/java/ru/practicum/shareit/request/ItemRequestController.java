package ru.practicum.shareit.request;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.CommonConstants;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient requestClient;
    private static final String NEGATIVE_REQUEST_ID_MESSAGE = "id запроса не может быть отрицательным";

    @PostMapping
    public ResponseEntity<Object> addRequest(@PositiveOrZero(message = CommonConstants.NEGATIVE_USER_ID_MESSAGE)
                                             @RequestHeader(CommonConstants.USER_ID_HEADER) long userId,
                                             @Validated @RequestBody ItemRequestDto itemRequest) {
        log.info("Поступил запрос на добавление ItemRequest от пользователя с id: {} с телом: {}", userId, itemRequest);
        return requestClient.addRequest(userId, itemRequest);
    }


    @GetMapping
    public ResponseEntity<Object> getUserRequests(@PositiveOrZero(message = CommonConstants.NEGATIVE_USER_ID_MESSAGE)
                                                  @RequestHeader(CommonConstants.USER_ID_HEADER) long userId) {
        log.info("Поступил запрос на получение всех ItemRequest пользователя с id: {}", userId);
        return requestClient.getUserRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@PositiveOrZero(message = NEGATIVE_REQUEST_ID_MESSAGE)
                                                 @PathVariable long requestId) {
        log.info("Поступил запрос на получение ItemRequest по id: {}", requestId);
        return requestClient.getRequestById(requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@PositiveOrZero(message = CommonConstants.NEGATIVE_USER_ID_MESSAGE)
                                                 @RequestHeader(CommonConstants.USER_ID_HEADER) long userId) {
        log.info("Поступил запрос на получение всех ItemRequest созданных другими пользователями");
        return requestClient.getAllRequests(userId);
    }
}
