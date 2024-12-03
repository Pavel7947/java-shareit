package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;

@Entity
@Table(name = "requests")
@Data
@Builder(toBuilder = true)
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private long id;
    @Column(nullable = false, length = 1200)
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User requester;
    @Column(name = "create_date", nullable = false)
    private Instant createDate;
}
