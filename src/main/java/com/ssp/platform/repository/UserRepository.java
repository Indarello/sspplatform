package com.ssp.platform.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ssp.platform.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>
{
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean deleteByUsername(String username);

    Page<User> findAllByRole(Pageable pageable, String role);
}
