package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.StateBooking;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplIntTest {
    private final BookingServiceImpl bookingService;
    private static final long BOOKER_ID = 3; //This user is the owner of 2 bookings in the past


    @Test
    void getAllUserBooking_WhenStateIsEqualAll_thenReturnBooking() {
        List<BookingDto> bookings = bookingService.getAllUserBooking(BOOKER_ID, StateBooking.ALL);
        assertEquals(2, bookings.size());

        long bookerId = bookings.getFirst().getBooker().getId();
        assertEquals(BOOKER_ID, bookerId);
        bookerId = bookings.getLast().getBooker().getId();
        assertEquals(BOOKER_ID, bookerId);
    }

    @Test
    void getAllUserBooking_WhenStateIsEqualFUTURE_thenNotReturnBooking() {
        List<BookingDto> bookings = bookingService.getAllUserBooking(BOOKER_ID, StateBooking.FUTURE);

        assertTrue(bookings.isEmpty());
    }
}