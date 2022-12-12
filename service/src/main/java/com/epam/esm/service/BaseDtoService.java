package com.epam.esm.service;

import com.epam.esm.dto.BaseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface represents dto service
 *
 * @param <T> the type parameter
 */
public interface BaseDtoService<T extends BaseDto> {
    /**
     * Finds all dto
     *
     * @param pageable the pagination information
     * @return list of found dto
     */
    Page<T> findAll(Pageable pageable);

    /**
     * Finds a dto by its id
     *
     * @param id dto id
     * @return found dto
     */
    T findById(long id);

    /**
     * Inserts dto
     *
     * @param dto dto
     * @return inserted dto
     */
    T save(T dto);

    /**
     * Deletes a dto by its id
     *
     * @param id dto id
     */
    void deleteById(long id);
}
