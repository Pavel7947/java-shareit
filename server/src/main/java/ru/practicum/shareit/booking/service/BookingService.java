package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoAddRequest;
import ru.practicum.shareit.booking.model.StateBooking;

import java.util.List;

public interface BookingService {

    BookingDto addBooking(long userId, BookingDtoAddRequest bookingDtoRequest);

    BookingDto approveOrRejectBooking(long userId, long bookingId, boolean approved);

    BookingDto getBookingById(long userId, long bookingId);

    List<BookingDto> getAllUserBooking(long ownerId, StateBooking stateBooking);

    List<BookingDto> getAllBookingForUsersItems(long ownerId, StateBooking stateBooking);

}
