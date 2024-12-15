package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemReqDtoExtendsResp;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceImplIntTest {
    private final ItemRequestServiceImpl requestService;
    private static final long REQUEST_ID_EXISTS = 1; //This request exists in database
    private static final long REQUEST_ID_NOT_EXISTS = 0; // This request is not exists in database
    private static final long REQUESTER_ID = 3; // This user owner two requests
    private static final long USER_ID_NOT_HAVING_REQUESTS = 2; // This user not having requests

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
    }

    @Test
    void getUserRequests_whenUserNotHavingRequests_thenReturnEmptyList() {
        List<ItemReqDtoExtendsResp> requests = requestService.getUserRequests(USER_ID_NOT_HAVING_REQUESTS);
        assertTrue(requests.isEmpty());
    }
}