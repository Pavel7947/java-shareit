package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemReqDtoAddRequest;
import ru.practicum.shareit.request.dto.ItemReqDtoExtendsResp;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto addRequest(long userId, ItemReqDtoAddRequest request);

    List<ItemRequestDto> getAllRequests(long userId);

    ItemReqDtoExtendsResp getRequestById(long requestId);

    List<ItemReqDtoExtendsResp> getUserRequests(long requesterId);

}
