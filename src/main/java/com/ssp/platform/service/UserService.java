package com.ssp.platform.service;

import com.ssp.platform.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для работы с сущностью List
 */
public interface UserService
{
    void save(User user);

    Optional<User> findByUsername(String username);

    Page<User> findAllByRole(Pageable pageable, String role);

    boolean deleteUser(String username);
}
