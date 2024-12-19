package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDtoOnlyDates;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoExtendsResp;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.common.CommonConstants.USER_ID_HEADER;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @MockBean
    ItemService itemService;
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;
    private ItemDto itemDto;
    private ItemDtoExtendsResp itemDtoExtendsResp;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        itemDto = ItemDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .requestId(1L)
                .build();
        itemDtoExtendsResp = ItemDtoExtendsResp.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .lastBooking(BookingDtoOnlyDates.builder()
                        .start(now.plusDays(5))
                        .end(now.plusDays(6)).build())
                .nextBooking(BookingDtoOnlyDates.builder()
                        .start(now.plusDays(2))
                        .end(now.plusDays(3)).build())
                .build();
    }

    @Test
    void createItem_whenInvoked_thenReturnItemDto() throws Exception {
        when(itemService.addItem(1, itemDto)).thenReturn(itemDto);
        mvc.perform(post("/items")
                        .header(USER_ID_HEADER, 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDto)));
    }

    @Test
    void updateItem_whenInvoked_thenReturnItemDto() throws Exception {
        when(itemService.updateItem(1, 1, itemDto)).thenReturn(itemDto);
        mvc.perform(patch("/items/" + 1)
                        .header(USER_ID_HEADER, 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDto)));
    }


    @Test
    void getItemDtoById_whenInvoked_thenReturnItemDto() throws Exception {
        when(itemService.getItemDtoById(1)).thenReturn(itemDtoExtendsResp);
        mvc.perform(get("/items/" + 1)
                        .header(USER_ID_HEADER, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDtoExtendsResp)));
    }

    @Test
    void getListItemDtoByUserId_whenInvoked_thenReturnListItemDto() throws Exception {
        when(itemService.getListItemDtoByUserId(1)).thenReturn(List.of(itemDtoExtendsResp));
        mvc.perform(get("/items")
                        .header(USER_ID_HEADER, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemDtoExtendsResp))));
    }

    @Test
    void getListItemDtoBySubstring_whenInvoked_thenReturnListItemDto() throws Exception {
        when(itemService.getListItemDtoBySubstring("text")).thenReturn(List.of(itemDto));
        mvc.perform(get("/items/search")
                        .param("text", "text")
                        .header(USER_ID_HEADER, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemDto))));
    }

    @Test
    void addComment_whenInvoked_thenReturnCommentDto() throws Exception {
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("text")
                .authorName("authorName")
                .created(LocalDateTime.now())
                .build();
        when(itemService.addComment(1, 1, commentDto)).thenReturn(commentDto);
        mvc.perform(post("/items/" + 1 + "/comment")
                        .header(USER_ID_HEADER, 1)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(commentDto)));
    }

    @Test
    void getItemDtoById_whenItemNotFound_thenReturnNotFound() throws Exception {
        when(itemService.getItemDtoById(1)).thenThrow(NotFoundException.class);
        mvc.perform(get("/items/" + 1)
                        .header(USER_ID_HEADER, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getListItemDtoByUserId_whenUserNotFound_thenReturnNotFound() throws Exception {
        when(itemService.getListItemDtoByUserId(1)).thenThrow(NotFoundException.class);
        mvc.perform(get("/items")
                        .header(USER_ID_HEADER, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


}