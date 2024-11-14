package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto getItemDtoById(long itemId);

    List<ItemDto> getListItemDtoByUserId(long userId);

    List<ItemDto> getListItemDtoBySubstring(String text);

    ItemDto addItem(long ownerId, ItemDto itemDto);

    ItemDto updateItem(long itemId, long ownerId, ItemDto itemDto);
}