package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoAddRequest;
import ru.practicum.shareit.booking.model.StateBooking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.common.CommonConstants;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(@RequestHeader(CommonConstants.USER_ID_HEADER) long userId,
                                 @RequestBody BookingDtoAddRequest bookingDto) {
        log.info("Поступил запрос на создание бронирования от пользователя с id: {}, с телом: {}", userId, bookingDto);
        return bookingService.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveOrRejectBooking(@RequestHeader(CommonConstants.USER_ID_HEADER) long userId,
                                             @PathVariable long bookingId, @RequestParam boolean approved) {
        log.info("Поступил запрос на подтверждение или отклонение бронирования c id: {} от пользователя с id: {}," +
                " с параметром approved {}", bookingId, userId, approved);
        return bookingService.approveOrRejectBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(CommonConstants.USER_ID_HEADER) long userId,
                                     @PathVariable long bookingId) {
        log.info("Поступил запрос на получение BookingDto по id: {} от пользователя с id: {}", bookingId, userId);
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getAllUserBooking(@RequestHeader(CommonConstants.USER_ID_HEADER) long userId,
                                              @RequestParam(defaultValue = "ALL") StateBooking state) {
        log.info("Поступил запрос на получение списка бронирований со статусом {} пользователя с id: {}", userId, state);
        return bookingService.getAllUserBooking(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingForUsersItems(@RequestHeader(CommonConstants.USER_ID_HEADER) long userId,
                                                       @RequestParam(defaultValue = "ALL") StateBooking state) {
        log.info("Поступил запрос на получение списка бронирований со статусом {} для всех вещей " +
                "пользователя id {}", userId, state);
        return bookingService.getAllBookingForUsersItems(userId, state);
    }
}
