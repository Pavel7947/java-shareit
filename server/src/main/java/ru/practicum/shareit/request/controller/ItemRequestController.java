package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.CommonConstants;
import ru.practicum.shareit.request.dto.ItemReqDtoAddRequest;
import ru.practicum.shareit.request.dto.ItemReqDtoExtendsResp;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestService requestService;

    @PostMapping
    public ItemRequestDto addRequest(@RequestHeader(CommonConstants.USER_ID_HEADER) long userId,
                                     @RequestBody ItemReqDtoAddRequest itemRequest) {
        log.info("Поступил запрос на добавление ItemRequest от пользователя с id: {} с телом: {}", userId, itemRequest);
        return requestService.addRequest(userId, itemRequest);
    }

    @GetMapping
    public List<ItemReqDtoExtendsResp> getUserRequests(@RequestHeader(CommonConstants.USER_ID_HEADER) long userId) {
        log.info("Поступил запрос на получение всех ItemRequest пользователя с id: {}", userId);
        return requestService.getUserRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemReqDtoExtendsResp getRequestById(@PathVariable long requestId) {
        log.info("Поступил запрос на получение ItemRequest по id: {}", requestId);
        return requestService.getRequestById(requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests(@RequestHeader(CommonConstants.USER_ID_HEADER) long userId,
                                               @RequestParam int from, @RequestParam int size) {
        log.info("Поступил запрос на получение всех ItemRequest созданных другими пользователями");
        return requestService.getAllRequests(userId, from, size);
    }
}
