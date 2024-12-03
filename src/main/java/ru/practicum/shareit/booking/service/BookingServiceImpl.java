package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForRequest;
import ru.practicum.shareit.booking.mapper.BookingDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StateBooking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto addBooking(long userId, BookingDtoForRequest dto) {
        long itemId = dto.getItemId();
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id: " + itemId + " не найдена"));
        if (!item.getAvailable()) {
            throw new BadRequestException("Вещь с id: " + itemId + " недоступна для аренды");
        }
        Booking savedBooking = BookingDtoMapper.toBooking(dto, item, booker, StatusBooking.WAITING);
        if (bookingRepository.existsByItemIdAndIntersection(itemId, savedBooking.getStart(), savedBooking.getEnd())) {
            throw new ConflictException("Добаляемое бронирование пересекается во времени с существующими");
        }
        return BookingDtoMapper.toBookingDto(bookingRepository.save(savedBooking));
    }

    @Override
    public BookingDto approveOrRejectBooking(long userId, long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id: " + bookingId + " не найдено"));
        if (booking.getItem().getOwner().getId() != userId) {
            throw new ForbiddenException("Пользователь с id: " + userId + " не является владельцем вещи " +
                    "для которой сформирован запрос на бронирование с id: " + bookingId);
        }
        if (approved) {
            booking.setStatus(StatusBooking.APPROVED);
        } else {
            booking.setStatus(StatusBooking.REJECTED);
        }
        return BookingDtoMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingDtoById(long userId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id: " + bookingId + " не найдено"));
        if (!(booking.getItem().getOwner().getId() == userId || booking.getBooker().getId() == userId)) {
            throw new ForbiddenException("Пользователь с id: " + userId + " не может просматривать информацию " +
                    "о бронировании  с id: " + bookingId + " так как он не является владельцем вещи или автором бронирования");
        }
        return BookingDtoMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllUserBookingDto(long ownerId, StateBooking stateBooking) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + ownerId + " не найден"));
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        List<Booking> bookings;
        return switch (stateBooking) {
            case FUTURE -> {
                bookings = bookingRepository.findAllByBookerIdAndStartAfter(ownerId, Instant.now(), sort);
                yield BookingDtoMapper.toBookingDto(bookings);
            }
            case PAST -> {
                bookings = bookingRepository.findAllByBookerIdAndEndBefore(ownerId, Instant.now(), sort);
                yield BookingDtoMapper.toBookingDto(bookings);
            }
            case CURRENT -> {
                bookings = bookingRepository.findAllByBookerIdAndCurrent(ownerId, Instant.now(), sort);
                yield BookingDtoMapper.toBookingDto(bookings);
            }
            case WAITING -> {
                bookings = bookingRepository.findAllByBookerIdAndStatus(ownerId, StatusBooking.WAITING, sort);
                yield BookingDtoMapper.toBookingDto(bookings);
            }
            case REJECTED -> {
                bookings = bookingRepository.findAllByBookerIdAndStatus(ownerId, StatusBooking.REJECTED, sort);
                yield BookingDtoMapper.toBookingDto(bookings);
            }
            default -> BookingDtoMapper.toBookingDto(bookingRepository.findAllByBookerId(ownerId, sort));
        };
    }

    @Override
    public List<BookingDto> getAllBookingDtoForUsersItems(long ownerId, StateBooking stateBooking) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + ownerId + " не найден"));
        List<Booking> bookings;
        return switch (stateBooking) {
            case FUTURE -> {
                bookings = bookingRepository.findAllByItemOwnerIdAndStartAfter(ownerId, Instant.now());
                yield BookingDtoMapper.toBookingDto(bookings);
            }
            case PAST -> {
                bookings = bookingRepository.findAllByItemOwnerIdAndEndBefore(ownerId, Instant.now());
                yield BookingDtoMapper.toBookingDto(bookings);
            }
            case CURRENT -> {
                bookings = bookingRepository.findAllByItemOwnerIdAndCurrent(ownerId, Instant.now());
                yield BookingDtoMapper.toBookingDto(bookings);
            }
            case WAITING -> {
                bookings = bookingRepository.findAllByItemOwnerIdAndStatus(ownerId, StatusBooking.WAITING);
                yield BookingDtoMapper.toBookingDto(bookings);
            }
            case REJECTED -> {
                bookings = bookingRepository.findAllByItemOwnerIdAndStatus(ownerId, StatusBooking.REJECTED);
                yield BookingDtoMapper.toBookingDto(bookings);
            }
            default -> BookingDtoMapper.toBookingDto(bookingRepository.findAllByItemOwnerId(ownerId));
        };
    }
}
