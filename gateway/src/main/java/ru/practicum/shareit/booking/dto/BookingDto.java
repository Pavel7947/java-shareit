package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {
    private long itemId;
    @Future(message = "Дата начала аренды должна быть в будущем")
    @NotNull(message = "Дата начала аренды обязательно должна быть указана")
    private LocalDateTime start;
    @Future(message = "Дата окончания аренды должна быть в будущем")
    @NotNull(message = "Дата окончания аренды обязательно должна быть указана")
    private LocalDateTime end;
}
