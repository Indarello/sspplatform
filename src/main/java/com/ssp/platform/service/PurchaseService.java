package com.ssp.platform.service;

import com.ssp.platform.entity.Purchase;
import com.ssp.platform.exceptions.SupplyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public interface PurchaseService {

	Purchase save(Purchase purchase);

	Purchase get(UUID id);

	Page<Purchase> getAll(Pageable pageable);

	Optional<Purchase> changePurchase(Purchase purchase);

	boolean deletePurchase(Purchase purchase) throws IOException, SupplyException;

	Optional<Purchase> findById(UUID id);

	boolean existById(UUID id);
}
