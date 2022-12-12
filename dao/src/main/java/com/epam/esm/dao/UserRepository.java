package com.epam.esm.dao;

import com.epam.esm.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {
    Optional<User> findByName(String name);
}
