package com.ssp.platform.service.impl;

import com.ssp.platform.entity.User;
import com.ssp.platform.repository.UserRepository;
import com.ssp.platform.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для выполнения операций с данными пользователя.
 * Реализация интерфейса
 */
@Service
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public void save(User user)
    {
        userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username)
    {
        return userRepository.findByUsername(username);
    }

    @Override
    public Page<User> findAllByRole(Pageable pageable, String role)
    {
        return userRepository.findAllByRole(pageable, role);
    }

    @Override
    @Transactional
    public boolean deleteUser(String username) {
        Optional<User> searchResult = userRepository.findByUsername(username);
        if (searchResult.isPresent()) {

            userRepository.deleteByUsername(username);
            return true;
        }

        return false;
    }
}
