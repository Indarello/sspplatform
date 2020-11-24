package com.ssp.platform.service;

import com.ssp.platform.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

/**
 * Сервис для работы с сущностью List
 */
public interface UserService
{
    void save(User user);

    boolean existsByRole(String role);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByInn(String tIN);

    boolean existsByPassword(String password);

    Page<User> findAllByRole(Pageable pageable, String role);

    Optional<User> findByUsername(String username);
}
