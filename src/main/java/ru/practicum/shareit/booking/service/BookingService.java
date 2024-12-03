package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForRequest;
import ru.practicum.shareit.booking.model.StateBooking;

import java.util.List;

public interface BookingService {

    BookingDto addBooking(long userId, BookingDtoForRequest bookingDtoRequest);

    BookingDto approveOrRejectBooking(long userId, long bookingId, boolean approved);

    BookingDto getBookingDtoById(long userId, long bookingId);

    List<BookingDto> getAllUserBookingDto(long ownerId, StateBooking stateBooking);

    List<BookingDto> getAllBookingDtoForUsersItems(long ownerId, StateBooking stateBooking);

}
