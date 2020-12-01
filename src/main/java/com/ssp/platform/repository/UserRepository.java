package com.ssp.platform.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ssp.platform.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByRole(String role);

    boolean existsByEmail(String email);

    boolean existsByTelephone(String telephone);

    boolean existsByInn(String tIN);

    Page<User> findAllByRole(Pageable pageable, String role);
}
