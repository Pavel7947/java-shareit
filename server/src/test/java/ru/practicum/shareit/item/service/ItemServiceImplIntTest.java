package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoExtendsResp;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplIntTest {
    private final ItemServiceImpl itemService;
    private static final long USER_ID_HAVING_ITEMS = 1; // This user have three items
    private static final long USER_ID_NOT_HAVING_ITEMS = 3; // This user does not have items
    private static final long ITEM_ID_EXIST = 1;// This item exists in the database
    private static final long ITEM_ID_NOT_EXIST = 0; // This item not exist in the database
    private static final long ITEM_ID = 3; //This item belongs to USER_ID_HAVING_ITEMS
    private static final long USER_ID_OWNER_APPROVED_BOOKING = 3; // This user owner approved booking
    private static final long ITEM_ID_WITH_APPROVED_BOOKING = 1; /*This item have approved booking from user:
                                                                  USER_ID_OWNER_APPROVED_BOOKING*/
    private static final long REQUEST_ID_NOT_EXIST = 0; //This ItemRequest not exist in the database
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        itemDto = ItemDto.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();
    }

    @Test
    void getListItemDtoByUserId_whenUserHaveThreeItems_thenReturnTwoItems() {
        List<ItemDtoExtendsResp> items = itemService.getListItemDtoByUserId(USER_ID_HAVING_ITEMS);
        assertEquals(3, items.size());
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

    @Test
    void addItem_whenItemNotConflict_thenReturnItem() {
        assertNotNull(itemService.addItem(USER_ID_NOT_HAVING_ITEMS, itemDto));
    }

    @Test
    void addItem_whenItemRequestNotFound_thenReturnNotFound() {
        itemDto.setRequestId(REQUEST_ID_NOT_EXIST);
        assertThrows(NotFoundException.class, () -> itemService.addItem(USER_ID_NOT_HAVING_ITEMS, itemDto));
    }

    @Test
    void updateItem_whenItemNotConflict_thenReturnItem() {
        assertNotNull(itemService.updateItem(USER_ID_HAVING_ITEMS, ITEM_ID, itemDto));
    }

    @Test
    void addComment_whenUserHaveBookingThisItem_whenReturnCommentDto() {
        CommentDto commentDto = CommentDto.builder()
                .text("comment")
                .build();
        assertNotNull(itemService.addComment(USER_ID_OWNER_APPROVED_BOOKING, ITEM_ID_WITH_APPROVED_BOOKING, commentDto));
    }
}