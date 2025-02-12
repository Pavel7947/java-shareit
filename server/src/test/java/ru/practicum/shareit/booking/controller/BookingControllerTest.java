package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoAddRequest;
import ru.practicum.shareit.booking.model.StateBooking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.common.CommonConstants.USER_ID_HEADER;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @MockBean
    private BookingService bookingService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    private BookingDtoAddRequest requestBooking;
    private BookingDto responseBooking;

    @BeforeEach
    void setUp() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusMinutes(5);
        responseBooking = BookingDto.builder()
                .id(1L)
                .booker(UserDto.builder().id(1L).build())
                .item(ItemDto.builder().id(1L).build())
                .start(start)
                .end(end)
                .status(StatusBooking.WAITING)
                .build();
        requestBooking = BookingDtoAddRequest.builder()
                .id(1L)
                .itemId(1L)
                .start(start)
                .end(end)
                .build();
    }

    @Test
    void addBooking_whenInvoked_thenReturnBookingDto() throws Exception {
        when(bookingService.addBooking(1, requestBooking)).thenReturn(responseBooking);
        mvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, 1)
                        .content(mapper.writeValueAsString(requestBooking))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(responseBooking)));
    }

    @Test
    void addBooking_whenUserNotFound_thenReturnNotFound() throws Exception {
        when(bookingService.addBooking(1, requestBooking)).thenThrow(NotFoundException.class);
        mvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, 1)
                        .content(mapper.writeValueAsString(requestBooking))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void approveOrRejectBooking_whenInvoked_thenReturnBookingDto() throws Exception {
        responseBooking = responseBooking.toBuilder().status(StatusBooking.APPROVED).build();
        when(bookingService.approveOrRejectBooking(1, 1, true)).thenReturn(responseBooking);
        mvc.perform(patch("/bookings/" + 1)
                        .param("approved", "true")
                        .header(USER_ID_HEADER, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(responseBooking)));
    }

    @Test
    void getBookingById_whenInvoked_thenReturnBookingDto() throws Exception {
        when(bookingService.getBookingById(1, 1)).thenReturn(responseBooking);
        mvc.perform(get("/bookings/" + 1)
                        .header(USER_ID_HEADER, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(responseBooking)));
    }

    @Test
    void getAllUserBooking_whenInvoked_thenReturnBookingDto() throws Exception {
        when(bookingService.getAllUserBooking(1, StateBooking.ALL)).thenReturn(List.of(responseBooking));
        mvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(responseBooking))));
    }

    @Test
    void getAllBookingForUsersItems_whenInvoked_thenReturnBookingDto() throws Exception {
        when(bookingService.getAllBookingForUsersItems(1, StateBooking.ALL)).thenReturn(List.of(responseBooking));
        mvc.perform(get("/bookings/owner")
                        .header(USER_ID_HEADER, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(responseBooking))));
    }

    @Test
    void getBookingById_whenBookingNotFound_thenReturnNotFound() throws Exception {
        when(bookingService.getBookingById(1, 1)).thenThrow(NotFoundException.class);
        mvc.perform(get("/bookings/" + 1)
                        .header(USER_ID_HEADER, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());


    }
}