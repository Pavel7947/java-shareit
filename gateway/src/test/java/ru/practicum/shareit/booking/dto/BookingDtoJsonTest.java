package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingDtoJsonTest {
    private final JacksonTester<BookingDto> json;

    @Test
    void testBookingDto() throws Exception {
        String start = "2024-12-16T00:56:10.2323116"; // ISO 8601
        String end = "2024-12-17T00:56:10.2323116"; // ISO 8601
        BookingDto bookingDto = BookingDto.builder()
                .start(LocalDateTime.parse(start))
                .end(LocalDateTime.parse(end))
                .itemId(1)
                .build();

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(start);
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(end);
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
    }

}