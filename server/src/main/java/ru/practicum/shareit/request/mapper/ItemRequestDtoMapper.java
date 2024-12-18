package ru.practicum.shareit.request.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDtoForItemRequest;
import ru.practicum.shareit.request.dto.ItemReqDtoAddRequest;
import ru.practicum.shareit.request.dto.ItemReqDtoExtendsResp;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestDtoMapper {

    public static ItemReqDtoExtendsResp toItemReqDtoExtendsResp(ItemRequest itemRequest, List<ItemDtoForItemRequest> items) {
        return ItemReqDtoExtendsResp.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(LocalDateTime.ofInstant(itemRequest.getCreateDate(), ZoneId.systemDefault()))
                .items(items).build();
    }

    public static ItemRequest toItemRequest(ItemReqDtoAddRequest request) {
        return ItemRequest.builder()
                .description(request.getDescription())
                .build();
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest request) {
        return ItemRequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(LocalDateTime.ofInstant(request.getCreateDate(), ZoneId.systemDefault()))
                .build();
    }

    public static List<ItemRequestDto> toItemRequestDto(Collection<ItemRequest> requests) {
        return requests.stream().map(ItemRequestDtoMapper::toItemRequestDto).toList();
    }
}
