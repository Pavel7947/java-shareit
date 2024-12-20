package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoAddRequest;
import ru.practicum.shareit.booking.dto.BookingDtoOnlyDates;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserDtoMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public final class BookingDtoMapper {

    public Booking toBooking(BookingDtoAddRequest bookingDto, Item item, User booker, StatusBooking statusBooking) {
        return Booking.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart().atZone(ZoneId.systemDefault()).toInstant())
                .end(bookingDto.getEnd().atZone(ZoneId.systemDefault()).toInstant())
                .item(item)
                .booker(booker)
                .status(statusBooking)
                .build();
    }

    public BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(LocalDateTime.ofInstant(booking.getStart(), ZoneId.systemDefault()))
                .end(LocalDateTime.ofInstant(booking.getEnd(), ZoneId.systemDefault()))
                .booker(UserDtoMapper.toUserDto(booking.getBooker()))
                .item(ItemDtoMapper.toItemDto(booking.getItem()))
                .status(booking.getStatus())
                .build();
    }

    public List<BookingDto> toBookingDto(Iterable<Booking> bookings) {
        List<BookingDto> dtos = new ArrayList<>();
        for (Booking booking : bookings) {
            dtos.add(toBookingDto(booking));
        }
        return dtos;
    }

    public BookingDtoOnlyDates toOnlyDatesBookingDto(Booking booking) {
        return BookingDtoOnlyDates.builder()
                .start(LocalDateTime.ofInstant(booking.getStart(), ZoneId.systemDefault()))
                .end(LocalDateTime.ofInstant(booking.getEnd(), ZoneId.systemDefault()))
                .build();
    }
}
