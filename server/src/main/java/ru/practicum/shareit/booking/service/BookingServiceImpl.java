package ru.practicum.shareit.booking.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoAddRequest;
import ru.practicum.shareit.booking.mapper.BookingDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.QBooking;
import ru.practicum.shareit.booking.model.StateBooking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.common.exception.BadRequestException;
import ru.practicum.shareit.common.exception.ConflictException;
import ru.practicum.shareit.common.exception.ForbiddenException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public BookingDto addBooking(long userId, BookingDtoAddRequest dto) {
        long itemId = dto.getItemId();
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id: " + itemId + " не найдена"));
        if (!item.getAvailable()) {
            throw new BadRequestException("Вещь с id: " + itemId + " недоступна для аренды");
        }
        Booking savedBooking = BookingDtoMapper.toBooking(dto, item, booker, StatusBooking.WAITING);
        BooleanExpression condition = QBooking.booking.item.eq(item)
                .and(QBooking.booking.start.before(savedBooking.getEnd()))
                .and(QBooking.booking.end.after(savedBooking.getStart()));
        if (bookingRepository.exists(condition)) {
            throw new ConflictException("Добаляемое бронирование пересекается во времени с существующими");
        }
        return BookingDtoMapper.toBookingDto(bookingRepository.save(savedBooking));
    }

    @Transactional
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
        return BookingDtoMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getBookingById(long userId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id: " + bookingId + " не найдено"));
        if (!(booking.getItem().getOwner().getId() == userId || booking.getBooker().getId() == userId)) {
            throw new ForbiddenException("Пользователь с id: " + userId + " не может просматривать информацию " +
                    "о бронировании  с id: " + bookingId + " так как он не является владельцем вещи или автором бронирования");
        }
        return BookingDtoMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllUserBooking(long bookerId, StateBooking stateBooking) {
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + bookerId + " не найден"));
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        BooleanExpression condition = QBooking.booking.booker.eq(booker);
        if (!stateBooking.equals(StateBooking.ALL)) {
            condition = condition.and(makeConditionByState(stateBooking));
        }
        return BookingDtoMapper.toBookingDto(bookingRepository.findAll(condition, sort));

    }

    @Override
    public List<BookingDto> getAllBookingForUsersItems(long ownerId, StateBooking stateBooking) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + ownerId + " не найден"));
        BooleanExpression condition = QBooking.booking.item.owner.eq(owner);
        if (!stateBooking.equals(StateBooking.ALL)) {
            condition = condition.and(makeConditionByState(stateBooking));
        }
        return BookingDtoMapper.toBookingDto(bookingRepository.findAll(condition));
    }

    private BooleanExpression makeConditionByState(StateBooking stateBooking) {
        Instant now = Instant.now();
        return switch (stateBooking) {
            case FUTURE -> QBooking.booking.start.after(now);
            case PAST -> QBooking.booking.end.before(now);
            case CURRENT -> QBooking.booking.start.loe(now).and(QBooking.booking.end.goe(now));
            case REJECTED -> QBooking.booking.status.eq(StatusBooking.REJECTED);
            default -> QBooking.booking.status.eq(StatusBooking.WAITING);
        };
    }

}
