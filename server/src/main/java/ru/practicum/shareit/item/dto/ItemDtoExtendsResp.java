package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoOnlyDates;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDtoExtendsResp {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private List<String> comments;
    private BookingDtoOnlyDates nextBooking;
    private BookingDtoOnlyDates lastBooking;

}
