package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoExtendsResp;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplIntTest {
    private final ItemServiceImpl itemService;
    private static final long USER_ID_HAVING_ITEMS = 1; // This user have two items
    private static final long USER_ID_NOT_HAVING_ITEMS = 3; // This user does not have items
    private static final long ITEM_ID_EXIST = 3;// This item exists in the database
    private static final long ITEM_ID_NOT_EXIST = 0; // This item not exist in the database

    @Test
    void getListItemDtoByUserId_whenUserHaveTwoItems_thenReturnTwoItems() {
        List<ItemDtoExtendsResp> items = itemService.getListItemDtoByUserId(USER_ID_HAVING_ITEMS);
        assertEquals(2, items.size());
    }

    @Test
    void getListItemDtoByUserId_whenUserHaveNotItems_thenEmptyList() {
        List<ItemDtoExtendsResp> items = itemService.getListItemDtoByUserId(USER_ID_NOT_HAVING_ITEMS);
        assertTrue(items.isEmpty());
    }

    @Test
    void getItemDtoById_whenItemIsExists_thenReturnItem() {
        ItemDtoExtendsResp item = itemService.getItemDtoById(ITEM_ID_EXIST);
        assertNotNull(item);
    }

    @Test
    void getItemDtoById_whenItemIsNotExists_thenThrowNotFound() {
        assertThrows(NotFoundException.class, () -> itemService.getItemDtoById(ITEM_ID_NOT_EXIST));
    }


}