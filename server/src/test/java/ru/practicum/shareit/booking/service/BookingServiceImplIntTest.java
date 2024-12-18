package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoAddRequest;
import ru.practicum.shareit.booking.model.StateBooking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.common.exception.ForbiddenException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplIntTest {
    private final BookingServiceImpl bookingService;
    private static final long BOOKER_ID = 3; //This user is the owner of 2 bookings in the past
    private static final long BOOKING_ID = 1; //This booking belongs to BOOKER_ID
    private static final long OWNER_ID = 1;// This user is the owner of the item with two bookings
    private static final long BOOKING_WAITING_ID = 2; //This Booking has the waiting status, and it addresses OWNER_ID
    private static final long USER_ID_NOT_HAVING_BOOKINGS = 2; // This user does not have bookings
    private static final long USER_ID_NOT_HAVING_ITEMS = 3; // This user does not have items
    private static final long ITEM_ID_HAVING_TWO_BOOKINGS = 1; //This item having two bookings
    @Test
    void getAllUserBooking_WhenStateIsEqualAll_thenReturnBooking() {
        List<BookingDto> bookings = bookingService.getAllUserBooking(BOOKER_ID, StateBooking.ALL);
        assertEquals(4, bookings.size());

        long bookerId = bookings.getFirst().getBooker().getId();
        assertEquals(BOOKER_ID, bookerId);
        bookerId = bookings.getLast().getBooker().getId();
        assertEquals(BOOKER_ID, bookerId);
    }

    @Test
    void getAllUserBooking_WhenStateIsEqualFUTURE_thenReturnBooking() {
        List<BookingDto> bookings = bookingService.getAllUserBooking(BOOKER_ID, StateBooking.FUTURE);

        assertEquals(2, bookings.size());
    }

    @Test
    void addBooking_whenBookingNotConflictIntersection_thenReturnBooking() {
        BookingDtoAddRequest bookingDto = BookingDtoAddRequest.builder()
                .itemId(ITEM_ID_HAVING_TWO_BOOKINGS)
                .start(LocalDateTime.now().plusDays(8))
                .end(LocalDateTime.now().plusDays(9))
                .build();
        assertNotNull(bookingService.addBooking(1, bookingDto));
    }

    @Test
    void getAllBookingForUsersItems_whenUserHaveItems_thenReturnBooking() {
        List<BookingDto> bookings = bookingService.getAllBookingForUsersItems(OWNER_ID, StateBooking.ALL);

        assertEquals(4, bookings.size());
    }

    @Test
    void getBookingById_whenUserOwnerBooking_thenReturnBooking() {
        BookingDto bookingDto = bookingService.getBookingById(OWNER_ID, BOOKING_ID);

        assertNotNull(bookingDto);
    }

    @Test
    void getBookingById_whenUserNotOwnerBooking_thenReturnForbiddenException() {
        assertThrows(ForbiddenException.class, () -> bookingService
                .getBookingById(USER_ID_NOT_HAVING_BOOKINGS, BOOKING_ID));

    }

    @Test
    void approveOrRejectBooking_whenUserOwnerItem_thenReturnBooking() {
        BookingDto bookingDto = bookingService.approveOrRejectBooking(OWNER_ID, BOOKING_WAITING_ID, true);

        assertNotNull(bookingDto);
        assertEquals(StatusBooking.APPROVED, bookingDto.getStatus());
    }

    @Test
    void approveOrRejectBooking_whenUserNotOwnerItem_thenReturnBooking() {
        assertThrows(ForbiddenException.class, () -> bookingService.approveOrRejectBooking(USER_ID_NOT_HAVING_ITEMS,
                BOOKING_WAITING_ID, true));

    }

}