package com.ssp.platform.service;

import com.ssp.platform.entity.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.UUID;

public interface PurchaseService {

	Purchase save(Purchase purchase);

	Purchase get(UUID id);

	Page<Purchase> getAll(Pageable pageable);

	Optional<Purchase> changePurchase(Purchase purchase);

	boolean deletePurchase(UUID id);

	Optional<Purchase> findById(UUID id);

	boolean existById(UUID id);
}
