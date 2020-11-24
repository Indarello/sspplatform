package com.ssp.platform.repository;

import com.ssp.platform.entity.SupplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.UUID;

public interface SupplyRepository extends JpaRepository<SupplyEntity, UUID> {

}
