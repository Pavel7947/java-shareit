package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoExtendsResp;

import java.util.List;

public interface ItemService {

    ItemDtoExtendsResp getItemDtoById(long itemId);

    List<ItemDtoExtendsResp> getListItemDtoByUserId(long userId);

    List<ItemDto> getListItemDtoBySubstring(String text);

    ItemDto addItem(long ownerId, ItemDto itemDto);

    ItemDto updateItem(long ownerId, long itemId, ItemDto itemDto);

    CommentDto addComment(long userId, long itemId, CommentDto commentDto);
}