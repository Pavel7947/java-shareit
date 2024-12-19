package ru.practicum.shareit.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDtoForItemRequest;
import ru.practicum.shareit.request.dto.ItemReqDtoAddRequest;
import ru.practicum.shareit.request.dto.ItemReqDtoExtendsResp;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@UtilityClass
public class ItemRequestDtoMapper {

    public ItemReqDtoExtendsResp toItemReqDtoExtendsResp(ItemRequest itemRequest, List<ItemDtoForItemRequest> items) {
        return ItemReqDtoExtendsResp.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(LocalDateTime.ofInstant(itemRequest.getCreateDate(), ZoneId.systemDefault()))
                .items(items).build();
    }

    public ItemRequest toItemRequest(ItemReqDtoAddRequest request) {
        return ItemRequest.builder()
                .description(request.getDescription())
                .build();
    }

    public ItemRequestDto toItemRequestDto(ItemRequest request) {
        return ItemRequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(LocalDateTime.ofInstant(request.getCreateDate(), ZoneId.systemDefault()))
                .build();
    }
}
