package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select case when count(b) > 0 then true else false end from Booking b " +
            "where b.item.id = :itemId AND b.start < :end AND b.end > :start")
    boolean existsByItemIdAndIntersection(@Param("itemId") long itemId, @Param("start") Instant start,
                                          @Param("end") Instant end);

    List<Booking> findAllByBookerIdAndStatus(long bookerId, StatusBooking status, Sort sort);

    List<Booking> findAllByBookerIdAndStartAfter(long bookerId, Instant instant, Sort sort);

    List<Booking> findAllByBookerIdAndEndBefore(long bookerId, Instant instant, Sort sort);

    @Query("select b from Booking b where b.booker.id = :bookerId AND b.start <= :now AND b.end >= :now")
    List<Booking> findAllByBookerIdAndCurrent(@Param("bookerId") long bookerId, @Param("now") Instant instant, Sort sort);

    Optional<Booking> findByBookerIdAndItemIdAndEndBefore(long bookerId, long itemId, Instant instant);

    List<Booking> findAllByItemIdInAndStartAfter(Iterable<Long> itemId, Instant instant);

    List<Booking> findAllByItemIdAndStartAfter(long itemId, Instant instant);

    List<Booking> findAllByBookerId(long ownerId, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStatus(long ownerId, StatusBooking status);

    List<Booking> findAllByItemOwnerIdAndStartAfter(long ownerId, Instant instant);

    List<Booking> findAllByItemOwnerIdAndEndBefore(long ownerId, Instant instant);

    @Query("select b from Booking b where b.item.owner.id = :ownerId AND b.start <= :now AND b.end >= :now")
    List<Booking> findAllByItemOwnerIdAndCurrent(@Param("ownerId") long ownerId, @Param("now") Instant instant);

    List<Booking> findAllByItemOwnerId(long ownerId);
}
