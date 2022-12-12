package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface represents service for Order entity
 */
public interface OrderService {
    Page<OrderDto> findAll(Pageable pageable);

    /**
     * Finds an Order by its id
     *
     * @param id Order id
     * @return found Order
     */
    OrderDto findById(long id);

    /**
     * Finds an Orders by user id
     *
     * @param userId   user id
     * @param pageable the pagination information
     * @return page of found entities
     */
    Page<OrderDto> findByUserId(long userId, Pageable pageable);

    /**
     * Inserts Order
     *
     * @param order Order
     * @return inserted Order
     */
    OrderDto save(OrderDto order);
}
