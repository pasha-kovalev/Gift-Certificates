package com.epam.esm.dao;

import com.epam.esm.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends BaseRepository<Order> {
    Page<Order> findByUserId(long userId, Pageable pageable);
}
