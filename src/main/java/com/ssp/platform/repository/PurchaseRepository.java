package com.ssp.platform.repository;

import com.ssp.platform.entity.Purchase;
import com.ssp.platform.entity.User;
import com.ssp.platform.entity.enums.PurchaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, UUID>, JpaSpecificationExecutor<Purchase> {
    boolean existsById(UUID id);
    List<Purchase> findByStatusOrStatus(PurchaseStatus purchaseStatus, PurchaseStatus purchaseStatus2);
}
