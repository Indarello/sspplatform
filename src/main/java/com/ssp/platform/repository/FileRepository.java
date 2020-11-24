package com.ssp.platform.repository;

import com.ssp.platform.entity.FileEntity;
import com.ssp.platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FileRepository extends JpaRepository<FileEntity, UUID> {
    Optional<FileEntity> findById(UUID id);
}
