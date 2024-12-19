package ru.practicum.shareit.common.handlers;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.practicum.shareit.common.exception.BadRequestException;
import ru.practicum.shareit.common.exception.ConflictException;
import ru.practicum.shareit.common.exception.ForbiddenException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static ru.practicum.shareit.common.CommonConstants.USER_ID_HEADER;

@WebMvcTest(controllers = UserController.class)
class ErrorHandlerIntTest {
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mvc;

    @Test
    void handleConflictException_whenExceptionThrow_thenItsBeenProcessed() throws Exception {
        when(userService.getUserDtoById(1)).thenThrow(new ConflictException("Conflict Exception"));
        MvcResult result = performGetRequest();

        assertEquals(409, result.getResponse().getStatus());
        assertTrue(result.getResponse().getContentAsString().contains("\"error\":\"Conflict Exception\""));
    }

    @Test
    void handleNotFound_whenExceptionThrow_thenItsBeenProcessed() throws Exception {
        when(userService.getUserDtoById(1)).thenThrow(new NotFoundException("Not Found Exception"));
        MvcResult result = performGetRequest();

        assertEquals(404, result.getResponse().getStatus());
        assertTrue(result.getResponse().getContentAsString().contains("\"error\":\"Not Found Exception\""));
    }

    @Test
    void handleConstraintViolationException_whenExceptionThrow_thenItsBeenProcessed() throws Exception {
        when(userService.getUserDtoById(1)).thenThrow(new ConstraintViolationException("Constraint Violation", null));
        MvcResult result = performGetRequest();

        assertEquals(400, result.getResponse().getStatus());
        assertTrue(result.getResponse().getContentAsString().contains("\"error\":\"Constraint Violation\""));
    }

    @Test
    void handleBadRequestExceptions_whenExceptionThrow_thenItsBeenProcessed() throws Exception {
        when(userService.getUserDtoById(1)).thenThrow(new BadRequestException("BadRequestException"));
        MvcResult result = performGetRequest();

        assertEquals(400, result.getResponse().getStatus());
        assertTrue(result.getResponse().getContentAsString().contains("\"error\":\"BadRequestException\""));
    }

    @Test
    void handleForbiddenExceptions() throws Exception {
        when(userService.getUserDtoById(1)).thenThrow(new ForbiddenException("ForbiddenException"));
        MvcResult result = performGetRequest();

        assertEquals(403, result.getResponse().getStatus());
        assertTrue(result.getResponse().getContentAsString().contains("\"error\":\"ForbiddenException\""));
    }

    @Test
    void handleAllExceptions() throws Exception {
        when(userService.getUserDtoById(1)).thenThrow(new RuntimeException());
        MvcResult result = performGetRequest();
        assertEquals(500, result.getResponse().getStatus());
        assertTrue(result.getResponse().getContentAsString(StandardCharsets.UTF_8)
                .contains("\"error\":\"Ой у нас чтото сломалось :)\""));
    }

    private MvcResult performGetRequest() throws Exception {

        return mvc.perform(get("/users/" + 1)
                        .header(USER_ID_HEADER, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

    }
}