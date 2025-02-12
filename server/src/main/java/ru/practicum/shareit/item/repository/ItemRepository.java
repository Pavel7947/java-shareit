package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findByOwnerIdAndId(long userId, long itemId);

    @Query("select i from Item i " +
            "where (upper(i.name) like upper(concat('%', ?1, '%')) " +
            "or upper(i.description) like upper(concat('%', ?1, '%'))) AND i.available")
    Collection<Item> searchBySubstring(String text);

    Collection<Item> findAllByOwnerId(long userId);

    @Query("select i from Item i join fetch owner join fetch request where i.request.id in (:requestIds)")
    Collection<Item> findAllByRequestInWithLazyFields(@Param("requestIds") Collection<Long> requestIds);

    @Query("select i from Item i join fetch owner join fetch request where i.request.id = :requestId")
    Collection<Item> findAllByRequestWithLazyFields(@Param("requestId") long requestId);


}
