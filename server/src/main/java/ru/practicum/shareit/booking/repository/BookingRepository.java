package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.shareit.booking.model.Booking;

import java.time.Instant;
import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Long>, QuerydslPredicateExecutor<Booking> {

    Collection<Booking> findAllByItemIdInAndStartAfter(Iterable<Long> itemId, Instant instant);

    Collection<Booking> findAllByItemIdAndStartAfter(long itemId, Instant instant);
}
