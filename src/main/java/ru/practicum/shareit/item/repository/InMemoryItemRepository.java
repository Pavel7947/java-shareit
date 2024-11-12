package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> items;
    private long currentId;

    @Override
    public Optional<Item> getItemById(long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public List<Item> getListItemByUserId(long userId) {
        return items.values().stream().filter(item -> item.getOwner() != null && item.getOwner().getId() == userId).toList();
    }

    @Override
    public List<Item> getListItemBySubstring(String text) {
        String textLowerCase = text.toLowerCase();
        return items.values().stream().filter(item -> {
            String nameLowerCase = item.getName().toLowerCase();
            String descriptionLowerCase = item.getDescription().toLowerCase();
            return (nameLowerCase.contains(textLowerCase) || descriptionLowerCase.contains(textLowerCase))
                    && item.getAvailable();
        }).toList();
    }

    @Override
    public Item addItem(long ownerId, Item item) {
        long id = getCurrentId();
        item.setId(id);
        item.setOwner(User.builder().id(ownerId).build());
        items.put(id, item);
        return item;
    }

    @Override
    public Item updateItem(long itemId, Item item) {
        items.put(itemId, item);
        return item;
    }

    private long getCurrentId() {
        return currentId++;
    }
}
