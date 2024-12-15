package ru.practicum.shareit.booking;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.StateBooking;
import ru.practicum.shareit.common.CommonConstants;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;
    private static final String NEGATIVE_BOOKING_ID_MASSAGE = "id бронирования не может быть отрицательным";


    @PostMapping
    public ResponseEntity<Object> addBooking(@PositiveOrZero(message = CommonConstants.NEGATIVE_USER_ID_MESSAGE)
                                             @RequestHeader(CommonConstants.USER_ID_HEADER) long userId,
                                             @Validated @RequestBody BookingDto bookingDto) {
        log.info("Поступил запрос на создание бронирования от пользователя с id: {}, с телом: {}", userId, bookingDto);
        return bookingClient.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveOrRejectBooking(@PositiveOrZero(message = CommonConstants.NEGATIVE_USER_ID_MESSAGE)
                                                         @RequestHeader(CommonConstants.USER_ID_HEADER) long userId,
                                                         @PositiveOrZero(message = NEGATIVE_BOOKING_ID_MASSAGE)
                                                         @PathVariable long bookingId, @RequestParam boolean approved) {
        log.info("Поступил запрос на подтверждение или отклонение бронирования c id: {} от пользователя с id: {}," +
                " с параметром approved {}", bookingId, userId, approved);
        return bookingClient.approveOrRejectBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@PositiveOrZero(message = CommonConstants.NEGATIVE_USER_ID_MESSAGE)
                                                 @RequestHeader(CommonConstants.USER_ID_HEADER) long userId,
                                                 @PositiveOrZero(message = NEGATIVE_BOOKING_ID_MASSAGE)
                                                 @PathVariable long bookingId) {
        log.info("Поступил запрос на получение BookingDto по id: {} от пользователя с id: {}", bookingId, userId);
        return bookingClient.getBookingById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserBooking(@PositiveOrZero(message = CommonConstants.NEGATIVE_USER_ID_MESSAGE)
                                                    @RequestHeader(CommonConstants.USER_ID_HEADER) long userId,
                                                    @RequestParam(defaultValue = "ALL") StateBooking state) {
        log.info("Поступил запрос на получение списка бронирований со статусом {} пользователя с id: {}", userId, state);
        return bookingClient.getAllUserBooking(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingForUsersItems(@PositiveOrZero(message = CommonConstants.NEGATIVE_USER_ID_MESSAGE)
                                                             @RequestHeader(CommonConstants.USER_ID_HEADER) long userId,
                                                             @RequestParam(defaultValue = "ALL") StateBooking state) {
        log.info("Поступил запрос на получение списка бронирований со статусом {} для всех вещей " +
                "пользователя id {}", userId, state);
        return bookingClient.getAllBookingForUsersItems(userId, state);

    }
}