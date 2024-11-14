package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Optional<Item> getItemById(long itemId);

    List<Item> getListItemByUserId(long userId);

    List<Item> getListItemBySubstring(String text);

    Item addItem(long ownerId, Item item);

    Item updateItem(long itemId, Item item);
}
