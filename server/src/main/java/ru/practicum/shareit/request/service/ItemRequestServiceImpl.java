package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoForItemRequest;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemReqDtoAddRequest;
import ru.practicum.shareit.request.dto.ItemReqDtoExtendsResp;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestDtoMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRepository itemRepository;
    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public ItemRequestDto addRequest(long userId, ItemReqDtoAddRequest request) {
        User user = findUserById(userId);
        ItemRequest itemRequest = ItemRequestDtoMapper.toItemRequest(request);
        itemRequest.setRequester(user);
        return ItemRequestDtoMapper.toItemRequestDto(requestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> getAllRequests(long userId, int from, int size) {
        User user = findUserById(userId);
        Sort sort = Sort.by("createDate").descending();
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size, sort);
        return requestRepository.findAllByRequesterNot(user, page)
                .map(ItemRequestDtoMapper::toItemRequestDto)
                .getContent();
    }

    @Override
    public ItemReqDtoExtendsResp getRequestById(long requestId) {
        ItemRequest itemRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос по id " + requestId + " не найден"));
        List<ItemDtoForItemRequest> items = itemRepository.findAllByRequestWithLazyFields(requestId).stream()
                .map(ItemDtoMapper::toItemDtoForItemRequest).toList();
        return ItemRequestDtoMapper.toItemReqDtoExtendsResp(itemRequest, items);
    }

    @Override
    public List<ItemReqDtoExtendsResp> getUserRequests(long requesterId) {
        User user = findUserById(requesterId);
        List<ItemReqDtoExtendsResp> result = new ArrayList<>();
        Map<Long, ItemRequest> requests = requestRepository.findAllByRequester(user).stream()
                .collect(Collectors.toMap(ItemRequest::getId, Function.identity()));
        Map<Long, List<Item>> allItems = itemRepository.findAllByRequestInWithLazyFields(requests.keySet()).stream()
                .collect(Collectors.groupingBy(item -> item.getRequest().getId()));
        for (ItemRequest request : requests.values()) {
            long requestId = request.getId();
            List<ItemDtoForItemRequest> items = new ArrayList<>();
            if (allItems.containsKey(requestId)) {
                items.addAll(ItemDtoMapper.toItemDtoForItemRequest(allItems.get(requestId)));
            }
            result.add(ItemRequestDtoMapper.toItemReqDtoExtendsResp(request, items));
        }
        result.sort(Comparator.comparing(ItemReqDtoExtendsResp::getCreated).reversed());
        return result;
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден"));
    }
}
