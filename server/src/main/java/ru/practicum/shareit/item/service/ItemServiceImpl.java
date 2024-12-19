package ru.practicum.shareit.item.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoOnlyDates;
import ru.practicum.shareit.booking.mapper.BookingDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.QBooking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.common.exception.BadRequestException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoExtendsResp;
import ru.practicum.shareit.item.mapper.CommentDtoMapper;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository requestRepository;

    @Override
    public ItemDtoExtendsResp getItemDtoById(long itemId) {
        Item item = getItemById(itemId);
        List<String> comments = commentRepository.findAllByItemId(itemId).stream().map(Comment::getText).toList();
        List<Booking> bookings = bookingRepository.findAllByItemIdAndStartAfter(itemId, Instant.now()).stream()
                .sorted(Comparator.comparing(Booking::getStart)).toList();
        BookingDtoOnlyDates nextBooking = null;
        BookingDtoOnlyDates lastBooking = null;
        if (!bookings.isEmpty()) {
            nextBooking = BookingDtoMapper.toOnlyDatesBookingDto(bookings.getFirst());
            lastBooking = BookingDtoMapper.toOnlyDatesBookingDto(bookings.getLast());
        }
        return ItemDtoMapper.toItemDtoExtendsResp(item, nextBooking, lastBooking, comments);
    }

    @Override
    public List<ItemDtoExtendsResp> getListItemDtoByUserId(long userId) {
        getUserById(userId);
        List<ItemDtoExtendsResp> resultCollection = new ArrayList<>();
        Map<Long, Item> foundItems = itemRepository.findAllByOwnerId(userId).stream()
                .collect(Collectors.toMap(Item::getId, Function.identity()));
        Map<Long, List<Booking>> foundFutureBookings = bookingRepository
                .findAllByItemIdInAndStartAfter(foundItems.keySet(), Instant.now()).stream()
                .collect(Collectors.groupingBy(booking -> booking.getItem().getId()));
        Map<Long, List<Comment>> foundComments = commentRepository
                .findAllByItemIdIn(foundItems.keySet()).stream()
                .collect(Collectors.groupingBy(comment -> comment.getItem().getId()));
        for (Item item : foundItems.values()) {
            long itemId = item.getId();
            BookingDtoOnlyDates nextBooking = null;
            BookingDtoOnlyDates lastBooking = null;
            List<String> comments = null;
            if (foundComments.containsKey(itemId)) {
                comments = foundComments.get(itemId).stream().map(Comment::getText).toList();
            }
            if (foundFutureBookings.containsKey(itemId)) {
                List<Booking> allBookingsForItem = foundFutureBookings.get(itemId);
                allBookingsForItem.sort(Comparator.comparing(Booking::getStart));
                nextBooking = BookingDtoMapper.toOnlyDatesBookingDto(allBookingsForItem.getFirst());
                lastBooking = BookingDtoMapper.toOnlyDatesBookingDto(allBookingsForItem.getLast());
            }
            resultCollection.add(ItemDtoMapper.toItemDtoExtendsResp(item, nextBooking, lastBooking, comments));
        }
        return resultCollection;
    }

    @Override
    public List<ItemDto> getListItemDtoBySubstring(String text) {
        return itemRepository.searchBySubstring(text).stream().map(ItemDtoMapper::toItemDto).toList();
    }

    @Transactional
    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        User owner = getUserById(userId);
        ItemRequest itemRequest = null;
        Long requestId = itemDto.getRequestId();
        if (requestId != null) {
            itemRequest = requestRepository.findById(requestId)
                    .orElseThrow(() -> new NotFoundException("По переданному id: " + requestId + " ItemRequest не найден"));
        }
        Item item = ItemDtoMapper.toNewItem(itemDto, owner, itemRequest);
        return ItemDtoMapper.toItemDto(itemRepository.save(item));
    }

    @Transactional
    @Override
    public ItemDto updateItem(long ownerId, long itemId, ItemDto itemDto) {
        Item item = itemRepository.findByOwnerIdAndId(ownerId, itemId)
                .orElseThrow(() -> new NotFoundException("У пользователя с id: " + ownerId + " нет вещи с id: " + itemId));
        String name = itemDto.getName();
        if (name != null && !name.isBlank()) {
            item.setName(name);
        }
        String description = itemDto.getDescription();
        if (description != null && !description.isBlank()) {
            item.setDescription(description);
        }
        Boolean available = itemDto.getAvailable();
        if (available != null) {
            item.setAvailable(available);
        }
        return ItemDtoMapper.toItemDto(item);
    }

    @Transactional
    @Override
    public CommentDto addComment(long userId, long itemId, CommentDto commentDto) {
        User user = getUserById(userId);
        Item item = getItemById(itemId);
        BooleanExpression condition = QBooking.booking.booker.eq(user)
                .and(QBooking.booking.item.eq(item))
                .and(QBooking.booking.end.loe(Instant.now()));
        if (!bookingRepository.exists(condition)) {
            throw new BadRequestException("Пользователь не может оставлять комментарий к вещи которую  не брал в аренду");
        }
        return CommentDtoMapper.toCommentDto(commentRepository.save(CommentDtoMapper.toNewComment(commentDto, user, item)));
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден"));
    }

    private Item getItemById(long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id: " + itemId + " не найдена"));
    }


}
