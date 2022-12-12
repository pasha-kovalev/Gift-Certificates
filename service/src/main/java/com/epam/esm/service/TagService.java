package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface represents service for Tag entity
 */
public interface TagService extends BaseDtoService<TagDto> {
    Page<TagDto> findAll(Pageable pageable);

    /**
     * Find most used tag of user with the highest cost of all orders.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page<TagDto> findMostUsedTagOfUserWithHighestCostOfAllOrders(Pageable pageable);
}
