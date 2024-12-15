package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoOnlyDates;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoExtendsResp;
import ru.practicum.shareit.item.dto.ItemDtoForItemRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemDtoMapper {

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static ItemDtoExtendsResp toItemDtoExtendsResp(Item item, BookingDtoOnlyDates nextBooking,
                                                          BookingDtoOnlyDates lastBooking, List<String> comments) {
        return ItemDtoExtendsResp.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .comments(comments)
                .nextBooking(nextBooking)
                .lastBooking(lastBooking)
                .build();
    }


    public static Item toNewItem(ItemDto itemDto, User owner, ItemRequest request) {
        return Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(owner)
                .request(request)
                .build();
    }

    public static ItemDtoForItemRequest toItemDtoForItemRequest(Item item) {
        return ItemDtoForItemRequest.builder()
                .itemId(item.getId())
                .ownerId(item.getOwner().getId())
                .name(item.getName())
                .build();
    }

    public static List<ItemDtoForItemRequest> toItemDtoForItemRequest(List<Item> items) {
        return items.stream().map(ItemDtoMapper::toItemDtoForItemRequest).toList();
    }
}
