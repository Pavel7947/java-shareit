package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemReqDtoAddRequest;
import ru.practicum.shareit.request.dto.ItemReqDtoExtendsResp;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceImplIntTest {
    private final ItemRequestServiceImpl requestService;
    private static final long REQUEST_ID_EXISTS = 1; //This request exists in database
    private static final long REQUEST_ID_NOT_EXISTS = 0; // This request is not exists in database
    private static final long REQUESTER_ID = 4; // This user owner two requests
    private static final long USER_ID_NOT_HAVING_REQUESTS = 2; // This user not having requests
    private static final int NUMBER_ALL_REQUESTS = 4; // This number of all requests;

    @Test
    void getRequestById_whenRequestExists_thenReturnItemRequest() {
        assertNotNull(requestService.getRequestById(REQUEST_ID_EXISTS));
    }

    @Test
    void getRequestById_whenRequestNotExists_theThrowNotFound() {
        assertThrows(NotFoundException.class, () -> requestService.getRequestById(REQUEST_ID_NOT_EXISTS));
    }

    @Test
    void getUserRequests_whenUserHavingTwoRequests_thenReturnTwoItemRequest() {
        List<ItemReqDtoExtendsResp> requests = requestService.getUserRequests(REQUESTER_ID);
        assertEquals(2, requests.size());

        assertTrue(requests.stream().anyMatch(requestDto -> !requestDto.getItems().isEmpty()));
    }

    @Test
    void getUserRequests_whenOneRequestWithItemDto_thenReturnTwoItemRequest() {
    }

    @Test
    void getUserRequests_whenUserNotHavingRequests_thenReturnEmptyList() {
        List<ItemReqDtoExtendsResp> requests = requestService.getUserRequests(USER_ID_NOT_HAVING_REQUESTS);
        assertTrue(requests.isEmpty());
    }

    @Test
    void addRequest_whenRequestIsValid_thenReturnItemRequest() {
        ItemReqDtoAddRequest request = new ItemReqDtoAddRequest("I need an accordion");
        assertNotNull(requestService.addRequest(REQUESTER_ID, request));
    }

    @Test
    void getAllRequest_whenInvokedMethod_thenReturnAllRequest() {
        List<ItemRequestDto> requests = requestService.getAllRequests(REQUESTER_ID, 0, 20);

        assertFalse(requests.isEmpty());
        assertTrue(requests.size() < NUMBER_ALL_REQUESTS);
    }
}