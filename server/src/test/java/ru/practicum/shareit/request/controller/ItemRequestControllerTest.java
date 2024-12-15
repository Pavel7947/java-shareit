package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemReqDtoAddRequest;
import ru.practicum.shareit.request.dto.ItemReqDtoExtendsResp;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.common.CommonConstants.USER_ID_HEADER;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    @MockBean
    ItemRequestService requestService;
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;
    private ItemRequestDto itemRequestDto;
    private ItemReqDtoExtendsResp itemReqDtoExtendsResp;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("description")
                .created(now)
                .build();
        itemReqDtoExtendsResp = ItemReqDtoExtendsResp.builder()
                .id(1L)
                .description("description")
                .created(now)
                .items(List.of())
                .build();
    }

    @Test
    void addRequest_whenInvoked_thenReturnItemRequestDto() throws Exception {
        ItemReqDtoAddRequest itemReqDtoAddRequest = new ItemReqDtoAddRequest("description");
        when(requestService.addRequest(1, itemReqDtoAddRequest)).thenReturn(itemRequestDto);
        mvc.perform(post("/requests")
                        .header(USER_ID_HEADER, 1)
                        .content(mapper.writeValueAsString(itemReqDtoAddRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemRequestDto)));
    }

    @Test
    void getUserRequests_whenInvoked_thenReturnListItemRequestDto() throws Exception {
        when(requestService.getUserRequests(1)).thenReturn(List.of(itemReqDtoExtendsResp));
        mvc.perform(get("/requests")
                        .header(USER_ID_HEADER, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemReqDtoExtendsResp))));
    }

    @Test
    void getRequestById_whenInvoked_thenReturnItemRequestDto() throws Exception {
        when(requestService.getRequestById(1)).thenReturn(itemReqDtoExtendsResp);
        mvc.perform(get("/requests/" + 1)
                        .header(USER_ID_HEADER, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemReqDtoExtendsResp)));
    }

    @Test
    void getAllRequests_whenInvoked_thenReturnListItemRequestDto() throws Exception {
        when(requestService.getAllRequests(1)).thenReturn(List.of(itemRequestDto));
        mvc.perform(get("/requests/all")
                        .header(USER_ID_HEADER, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemRequestDto))));
    }

    @Test
    void getRequestById_whenRequestNotFound_thenReturnNotFound() throws Exception {
        when(requestService.getRequestById(1)).thenThrow(NotFoundException.class);
        mvc.perform(get("/requests/" + 1)
                        .header(USER_ID_HEADER, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUserRequests_whenUserNotFound_thenReturnNotFound() throws Exception {
        when(requestService.getUserRequests(1)).thenThrow(NotFoundException.class);
        mvc.perform(get("/requests")
                        .header(USER_ID_HEADER, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


}