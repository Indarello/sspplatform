package com.ssp.platform.repository;

import com.ssp.platform.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface SupplyRepository extends JpaRepository<SupplyEntity, UUID> {

    boolean existsByAuthorAndPurchaseId(User author, UUID purchaseId);

}
