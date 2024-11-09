package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemDtoMapper itemDtoMapper;

    @Override
    public ItemDto getItemDtoById(long itemId) {
        return itemDtoMapper.toItemDto(itemRepository.getItemById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id: " + itemId + " не найдена")));
    }

    @Override
    public List<ItemDto> getListItemDtoByUserId(long userId) {
        checkExistenceUser(userId);
        return getListItemByUserId(userId).stream().map(itemDtoMapper::toItemDto).toList();
    }

    @Override
    public List<ItemDto> getListItemDtoBySubstring(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        return itemRepository.getListItemBySubstring(text).stream().map(itemDtoMapper::toItemDto).toList();
    }

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        checkExistenceUser(userId);
        return itemDtoMapper.toItemDto(itemRepository.addItem(userId, itemDtoMapper.toItem(itemDto)));
    }

    @Override
    public ItemDto updateItem(long itemId, long userId, ItemDto itemDto) {
        checkExistenceUser(userId);
        Item oldItem = getListItemByUserId(userId).stream().filter(item -> item.getId() == itemId).findFirst()
                .orElseThrow(() -> new NotFoundException("У пользователя с id: " + userId + " нет вещи с id: " + itemId));
        Item newItem = itemDtoMapper.toItem(itemDto);
        String name = newItem.getName();
        if (name == null || name.isBlank()) {
            newItem.setName(oldItem.getName());
        }
        String description = newItem.getDescription();
        if (description == null || description.isBlank()) {
            newItem.setDescription(oldItem.getDescription());
        }
        if (newItem.getAvailable() == null) {
            newItem.setAvailable(oldItem.getAvailable());
        }
        return itemDtoMapper.toItemDto(itemRepository.updateItem(itemId, newItem));
    }

    private void checkExistenceUser(long userId) {
        userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден"));
    }

    private List<Item> getListItemByUserId(long userId) {
        return itemRepository.getListItemByUserId(userId);
    }

}
